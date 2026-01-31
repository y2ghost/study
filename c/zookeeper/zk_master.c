#include <zookeeper.h>
#include <zookeeper_log.h>
#include <assert.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <stdarg.h>
#include <unistd.h>
#include <pthread.h>

typedef struct String_vector string_vector;

static int server_id_ = 0;
static zhandle_t *zk_client_ = NULL;
static pthread_cond_t zk_cond_ = PTHREAD_COND_INITIALIZER;
static pthread_mutex_t zk_lock_ = PTHREAD_MUTEX_INITIALIZER;

static string_vector* tasks_ = NULL;
static string_vector* workers_ = NULL;

static void _create_parent();
static void _run_for_master();
static void _check_master();
static void _master_exists();
static void _get_workers();
static void _get_tasks();
static void _get_task_data();
static void _task_assignment();
static void _delete_pending_task();

struct task_info {
    char *name;
    char *value;
    int value_len;
    char *worker;
};

static const char *_event_string(int type)
{
    const char *str = NULL;
    switch (type) {
    case 1:
        str = "CREATED_EVENT";
        break;
    case 2:
        str = "DELETED_EVENT";
        break;
    case 3:
        str = "CHANGED_EVENT";
        break;
    case 4:
        str = "CHILD_EVENT";
        break;
    case -1:
        str = "SESSION_EVENT";
        break;
    case -2:
        str = "NOTWATCHING_EVENT";
        break;
    default:
        str = "UNKNOWN_EVENT_TYPE";
        break;
    }

    return str;
}

static string_vector *_make_copy(const string_vector *vector)
{
    string_vector *tmp_vector = malloc(sizeof(*tmp_vector));
    int count = vector->count;

    allocate_String_vector(tmp_vector, count);
    for (int i=0; i<count; i++) {
        tmp_vector->data[i] = strdup(vector->data[i]);
    }
    
    return tmp_vector;
}

static void _free_task_info(struct task_info *task)
{
    free(task->name);
    free(task->value);
    free(task->worker);
    free(task);
}

static int _contains(const char *child, const string_vector *children)
{
    int rc = 0;
    for (int i=0; i<children->count; i++) {
        if (0 == strcmp(child, children->data[i])) {
            rc = 1;
            break;
        }
    }

    return 0;
}

/* the set diff: belong a, not b */
static string_vector *_set_diff(const string_vector *a,
    const string_vector *b)
{
    int i = 0;
    int count = 0;
    int a_count = a->count;
    char **data_array = malloc(a_count * sizeof(*data_array));

    for (i=0; i<a_count; i++) {
        char *data = a->data[i];
        if (0 == _contains(data, b)) {
            data_array[count] = strdup(data);
            count++;
        }
    }

    string_vector *diff = malloc(sizeof(*diff));
    allocate_String_vector(diff, count);
    diff->count = count;

    for (i=0; i<count; i++) {
        diff->data[i] = data_array[i];
    }

    free(data_array);
    return diff;
}

/*
 * This function returns the elements that are new in current
 * compared to previous and update previous.
 */
static string_vector *_added_and_set(const string_vector *current,
    string_vector **previous)
{
    string_vector *prev = *previous;
    string_vector *diff = _set_diff(current, prev);
    deallocate_String_vector(prev);
    (*previous) = _make_copy(current);
    return diff;
}

/*
 * This function returns the elements that are have been removed in
 * compared to previous and update previous.
 */
static string_vector *_removed_and_set(const string_vector *current,
    string_vector **previous)
{
    string_vector *prev = *previous;
    string_vector *diff = _set_diff(prev, current);
    deallocate_String_vector(prev);
    (*previous) = _make_copy(current);
    return diff;
}

static void _main_watcher(zhandle_t *zkh, int type,
    int state, const char *path, void *context)
{
    if (ZOO_SESSION_EVENT == type) {
        if (ZOO_CONNECTED_STATE == state) {
            LOG_DEBUG(("Received a connected event."));
        } else if (ZOO_CONNECTING_STATE == state) {
            LOG_DEBUG(("Received a connecting event."));
        } else if (ZOO_EXPIRED_SESSION_STATE == state) {
            zookeeper_close(zkh);
            LOG_DEBUG(("Received a expired event."));
        }
       
        pthread_mutex_lock(&zk_lock_);
        pthread_cond_signal(&zk_cond_);
        pthread_mutex_unlock(&zk_lock_);
    }

    LOG_DEBUG(("Event: %s, %d", _event_string(type), state));
}

/*
 * Assign tasks, but first read task data. In this simple
 * implementation, there is no real task data in the znode,
 * but we include fetching the data to illustrate.
 */
static void _assign_tasks(const string_vector *strings)
{
    LOG_DEBUG(("Task count: %d", strings->count));
    for( int i=0; i<strings->count; i++) {
        LOG_DEBUG(("Assigning task %s", strings->data[i]));
        _get_task_data(strings->data[i]);
    }
}

/*
 * Completion function invoked when the call to get
 * the list of tasks returns.
 */
static void _tasks_completion(int rc, const string_vector *strings,
    const void *data)
{
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _get_tasks();
        break;
    case ZOK:
        LOG_DEBUG(("Assigning tasks"));
        string_vector *tmp_tasks = _added_and_set(strings, &tasks_);
        _assign_tasks(tmp_tasks);
        deallocate_String_vector(tmp_tasks);
        break;
    default:
        LOG_ERROR(("Something went wrong when checking "
            "tasks: %s", zerror(rc)));
        break;
    }
}

static void _tasks_watcher(zhandle_t *zh, int type, int state,
    const char *path, void *watcherCtx)
{
    LOG_DEBUG(("Tasks watcher triggered %s %d", path, state));
    if (type == ZOO_CHILD_EVENT) {
        _get_tasks();
    } else {
        LOG_INFO(("Watched event: %s", _event_string(type)));
    }

    LOG_DEBUG(("Tasks watcher done"));
}

void _get_tasks() {
    LOG_DEBUG(("Getting tasks"));
    zoo_awget_children(zk_client_, "/tasks",
        _tasks_watcher, NULL,
        _tasks_completion, NULL);
}

static void _delete_task_completion(int rc, const void *data)
{
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _delete_pending_task((const char *) data);
        break;
    case ZOK:
        LOG_DEBUG(("Deleted task: %s", (char *) data));
        free((char*)data);
        break;
    default:
        LOG_ERROR(("Something went wrong when deleting task: %s",
            zerror(rc)));
        break;
    }
}

static void _delete_pending_task(const char *path)
{
    if (NULL == path) {
        return;
    }

    char * tmp_path = strdup(path);
    zoo_adelete(zk_client_, tmp_path,
        -1, _delete_task_completion,
    (const void*)tmp_path);
}

static void _workers_completion (int rc, const string_vector *strings,
    const void *data)
{
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _get_workers();
        break;
    case ZOK:
        LOG_DEBUG(("Got %d workers", strings->count));
        string_vector *tmp_workers = _removed_and_set(strings, &workers_);
        deallocate_String_vector(tmp_workers);
        _get_tasks();
        break;
    default:
        LOG_ERROR(("Something wrong when checking workers: %s",
            zerror(rc)));
        break;
    }
}

static void _workers_watcher(zhandle_t *zh, int type, int state,
    const char *path, void *watcherCtx)
{
    if (type == ZOO_CHILD_EVENT) {
        _get_workers();
    } else {
        LOG_DEBUG(("Watched event: ", _event_string(type)));
    }
}

static void _get_workers(void)
{
    zoo_awget_children(zk_client_, "/workers",
        _workers_watcher, NULL,
        _workers_completion, NULL);
}

static void _take_leadership(void)
{
    _get_workers();
}

static void _master_check_completion (int rc, const char *value,
    int value_len, const struct Stat *stat, const void *data)
{
    int master_id = 0;
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _check_master();
        break;
    case ZOK:
        sscanf(value, "%x", &master_id);
        if (master_id == server_id_) {
            _take_leadership();
            LOG_DEBUG(("Elected primary master"));
        } else {
            _master_exists();
            LOG_DEBUG(("The primary is some other process"));
        }
            
        break;
    case ZNONODE:
        _run_for_master();
        break;
    default:
        LOG_ERROR(("Something went wrong when checking the "
            "master lock: %s", zerror(rc)));
        break;
    }
}

static void _check_master(void)
{
    zoo_aget(zk_client_, "/master",
        0, _master_check_completion, NULL);
}

static void _task_assignment_completion(int rc, const char *value,
    const void *data)
{
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _task_assignment((struct task_info*)data);
        break;
    case ZOK:
        if (NULL != data) {
            char del_path[1024] = {'\0'};
            struct task_info *task = (struct task_info*)data;
            LOG_DEBUG(("Deleting pending task %s", task->name));
            snprintf(del_path, sizeof(del_path), "/tasks/%s", task->name);
            _delete_pending_task(del_path);
            _free_task_info(task);
        }

        break;
    case ZNODEEXISTS:
        LOG_DEBUG(("Assignment has already been created: %s", value));
        break;
    default:
        LOG_ERROR(("Something went wrong when checking assignment "
            "completion: %s", zerror(rc)));
        break;
    }
}

static void _task_assignment(struct task_info *task)
{
    char path[1024] = {'\0'};
    snprintf(path, sizeof(path), "/assign/%s/%s",
        task->worker, task->name); 
    zoo_acreate(zk_client_, path, task->value, task->value_len,
        &ZOO_OPEN_ACL_UNSAFE, 0,
        _task_assignment_completion, (const void*)task);
}

static void _get_task_data_completion(int rc, const char *value,
    int value_len, const struct Stat *stat, const void *data)
{
    int worker_index = 0;
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _get_task_data((const char *) data);
        break;
    case ZOK:
        LOG_DEBUG(("Choosing worker for task %s", (const char *) data));
        if (NULL != workers_) {
            struct task_info *new_task = NULL;
            worker_index = (rand() % workers_->count);
            LOG_DEBUG(("Chosen worker %d %d",
                worker_index, workers_->count));
            new_task = (struct task_info*)malloc(sizeof(*new_task));
            new_task->name = (char *)data;
            new_task->value = strndup(value, value_len);
            new_task->value_len = value_len;
            const char * worker_string = workers_->data[worker_index];
            new_task->worker = strdup(worker_string);
            LOG_DEBUG(("Ready to assign it %d, %s",
                worker_index, workers_->data[worker_index]));
            _task_assignment(new_task);
        }

        break;
    default:
        LOG_ERROR(("Something went wrong when checking "
            "the master lock: %s", zerror(rc)));
        break;
    }
}

static void _get_task_data(const char *task)
{
    if (NULL == task) {
        return;
    }
    
    LOG_DEBUG(("Task path: %s", task));
    char *tmp_task = strndup(task, 15);
    char path[1024] = {'\0'};
    snprintf(path, sizeof(path), "/tasks/%s", tmp_task);
    LOG_DEBUG(("Getting task data %s", tmp_task));
    zoo_aget(zk_client_, path, 0, _get_task_data_completion,
        (const void*)tmp_task);
}

static void _master_exists_watcher(zhandle_t *zh, int type,
    int state, const char *path, void *watcherCtx)
{
    if (type == ZOO_DELETED_EVENT) {
        _run_for_master();
    } else {
        LOG_DEBUG(("Watched event: ", _event_string(type)));
    }
}

static void _master_exists_completion(int rc, const struct Stat *stat,
    const void *data)
{
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _master_exists();
        break;
    case ZOK:
        break;
    case ZNONODE:
        LOG_INFO(("Previous master is gone, running for master"));
        _run_for_master();
        break;
    default:
        LOG_WARN(("Something went wrong when "
            "executing exists: %s", zerror(rc)));
        break;
    }
}

static void _master_exists()
{
    zoo_awexists(zk_client_, "/master",
        _master_exists_watcher, NULL,
        _master_exists_completion, NULL);
}

static void _master_create_completion (int rc, const char *value,
    const void *data)
{
    switch (rc) {
    case ZCONNECTIONLOSS:
    case ZOPERATIONTIMEOUT:
        _check_master();
        break;
    case ZOK:
        _take_leadership();
        break;
    case ZNODEEXISTS:
        _master_exists();
        break;
    default:
        LOG_ERROR(("Something went wrong when running for master."));
        break;
    }
}

/* states as below:
 * ZOO_EXPIRED_SESSION_STATE
 * ZOO_AUTH_FAILED_STATE
 * ZOO_CONNECTING_STATE
 * ZOO_ASSOCIATING_STATE
 * ZOO_CONNECTED_STATE
 */
static void _wait_state(int state)
{
    pthread_mutex_lock(&zk_lock_);
    while (state != zoo_state(zk_client_)) {
        pthread_cond_wait(&zk_cond_, &zk_lock_);
    }

    pthread_mutex_unlock(&zk_lock_);
}

static void _run_for_master(void)
{
    char server_id_string[9] = {'\0'};
    int len = snprintf(server_id_string, 9, "%x", server_id_);

    _wait_state(ZOO_CONNECTED_STATE);
    zoo_acreate(zk_client_, "/master",
        server_id_string, 2,
        &ZOO_OPEN_ACL_UNSAFE, ZOO_EPHEMERAL,
        _master_create_completion, NULL);
}

static void _create_parent_completion(int rc, const char *value,
    const void * data)
{
    switch (rc) {
    case ZCONNECTIONLOSS:
        _create_parent(value);
        break;
    case ZOK:
        LOG_INFO(("Created parent node", value));
        break;
    case ZNODEEXISTS:
        LOG_WARN(("Node already exists"));
        break;
    default:
        LOG_ERROR(("Something went wrong when running "
            "for master: %s, %s", value, zerror(rc)));
        break;
    }
}

static void _create_parent(const char *path)
{
    zoo_acreate(zk_client_, path, NULL, -1,
        &ZOO_OPEN_ACL_UNSAFE, 0,
        _create_parent_completion, NULL);
}

static void _bootstrap()
{
    _wait_state(ZOO_CONNECTED_STATE);
    _create_parent("/workers");
    _create_parent("/assign");
    _create_parent("/tasks");
    _create_parent("/status");

    tasks_ = malloc(sizeof(*tasks_));
    allocate_String_vector(tasks_, 0);

    workers_ = malloc(sizeof(*workers_));
    allocate_String_vector(workers_, 0);
}

static int _init(char *host)
{
    char tmp_host[512] = {'\0'};
    snprintf(tmp_host, sizeof(tmp_host), "%s:2181", host);

    srand(time(NULL));
    server_id_ = rand();
    zoo_set_debug_level(ZOO_LOG_LEVEL_DEBUG);
    zk_client_ = zookeeper_init(tmp_host, _main_watcher,
        15000, NULL, NULL, 0);
    return errno;
}

int main(int ac, char * av[])
{
    if (2 != ac) {
        fprintf(stderr, "%s host\n", av[0]);
        exit(1);
    }

    char *host = av[1];
    if (0 != _init(host)) {
        LOG_ERROR(("failed to initialize master!"));
        exit(1);
    }
    
    LOG_INFO(("going to bootstrap and do master jobs"));
    _bootstrap();
    _run_for_master();
    _wait_state(ZOO_EXPIRED_SESSION_STATE);
    return 0; 
}
