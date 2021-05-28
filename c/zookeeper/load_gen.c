#include <zookeeper.h>
#include <zookeeper_log.h>
#include <errno.h>
#include <pthread.h>
#include <string.h>
#include <stdlib.h>

static zhandle_t *zh = NULL;
static pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
static pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;

static pthread_cond_t counter_cond = PTHREAD_COND_INITIALIZER;
static pthread_mutex_t counter_lock = PTHREAD_MUTEX_INITIALIZER;
static int counter = 0;  

static void _wait_connected(void)
{
    pthread_mutex_lock(&lock);
    while (zoo_state(zh) != ZOO_CONNECTED_STATE) {
        pthread_cond_wait(&cond, &lock);
    }

    pthread_mutex_unlock(&lock);
}

static void _inc_counter(int delta)
{
    pthread_mutex_lock(&counter_lock);
    counter += delta;
    pthread_cond_broadcast(&counter_cond);
    pthread_mutex_unlock(&counter_lock);        
}

static void _set_counter(int cnt)
{
    pthread_mutex_lock(&counter_lock);
    counter = cnt;
    pthread_cond_broadcast(&counter_cond);
    pthread_mutex_unlock(&counter_lock);        
}

static void _wait_counter()
{
    pthread_mutex_lock(&counter_lock);
    while (counter > 0) {
        pthread_cond_wait(&counter_cond, &counter_lock);
    }

    pthread_mutex_unlock(&counter_lock);    
}

static void _listener(zhandle_t *zzh, int type, int state,
    const char *path, void* ctx)
{
    if (ZOO_SESSION_EVENT == type) {
        if (ZOO_CONNECTED_STATE == state) {
            pthread_mutex_lock(&lock);
            pthread_cond_broadcast(&cond);
            pthread_mutex_unlock(&lock);
        }

        _set_counter(0);
    }
}

static void _create_completion(int rc, const char *name, const void *data)
{
    _inc_counter(-1);
    if (ZOK != rc) {
        LOG_ERROR(("Failed to create a node rc = %d",rc));
    }
}

static int _do_create_nodes(const char* path, int count)
{
    char node_name[1024] = {'\0'};
    int rc = 0;
    int i = 0;

    for (i=0; i<count; i++) {
        snprintf(node_name, sizeof(node_name), "%s/%d", path, i);
        _inc_counter(1);
        rc = zoo_acreate(zh, node_name, "create", 6, &ZOO_OPEN_ACL_UNSAFE, 0,
            create_completion, NULL);

        if (0 == i%1000) {
            LOG_INFO(("created number: %d", i));
        }

        if (ROK != rc) {
            break;
        }
    }

    return rc;
}

static int _create_parent(const char *path)
{
    char realpath[1024] = {'\0'};
    int rc = zoo_create(zh, path, "parent", 6, &ZOO_OPEN_ACL_UNSAFE,
        0, realpath, sizeof(realpath));
    return 0;
}

static void _write_completion(int rc, const struct Stat *stat, const void *data) {
    _inc_counter(-1);
    if (ZOK != rc) {
        LOG_ERROR(("failed to write a node rc = %d", rc));
    }
}

static int _do_writes(const char* path, int count){
    char node_name[1024] = {'\0'};
    int i = 0;
    int rc = 0;

    counter = 0;
    for (i=0; i<count; i++) {
        snprintf(node_name, sizeof(node_name), "%s/%d", path, i);
        _inc_counter(1);

        rc = zoo_aset(zh, node_name, "write", 5, -1,
            _write_completion, NULL);
        if (ZOK != rc) {
            break;
        }
    }

    return rc;
}

static void _read_completion(int rc, const char *value, int value_len,
    const struct Stat *stat, const void *data)
{
    _inc_counter(-1);
    if (ZOK != rc) {
        LOG_ERROR(("Failed to read a node rc = %d", rc));
        return;
    }

    if (0 != memcmp(value, "write", 5)) {
        char buf[value_len+1] = {'\0'};
        memcpy(buf, value, value_len);
        LOG_ERROR(("Invalid read, expected [second], received [%s]\n", buf));
        exit(1);
    }
}

static int _do_reads(const char* path, int count)
{
    char node_name[1024] = {'\0'};
    int rc = 0;
    int i = 0;

    counter = 0;
    for (i=0; i<count; i++) {
        snprintf(node_name, sizeof(node_name), "%s/%d", path, i);
        _inc_counter(1);

        rc = zoo_aget(zh, node_name, 0, _read_completion, NULL);
        if (ZOK != rc) {
            break;
        }
    }

    return rc;
}

static void _delete_completion(int rc, const void *data)
{
    _inc_counter(-1);    
}

static int _do_deletes(const char *path, int count)
{
    char node_name[1024] = {'\0'};
    int rc = 0;
    int i = 0;

    counter = 0;
    for (i=0; i<count; i++) {
        snprintf(node_name, sizeof(node_name), "%s/%d", path, i);
        _inc_counter(1);

        rc = zoo_adelete(zh, node_name, -1, delete_completion, NULL);
        if (ZOK != rc) {
            break;
        }
    }

    return rc;
}

static int delete_counter = 0;

static int _recursive_delete(const char *path)
{
    struct String_vector children = {0};
    int i = 0 ;
    int rc = zoo_get_children(zh, path, 0, &children);

    if (ZOK!=rc && ZNONODE!=rc) {
        LOG_ERROR(("Failed to get children of %s, rc = %d", path, rc));
        return rc;
    }

    for (i=0; i<children.count; i++) {
        char node_name[2048] = {'\0'};
        snprintf(node_name, sizeof(node_name), "%s/%s", path, children.data[i]);

        rc=_recursive_delete(node_name);
        if (ZOK != rc) {
            break;
        }
    }

    deallocate_String_vector(&children);
    if (ZOK != rc) {
        return rc;
    }

    if (0 == delete_counter%1000) {
        LOG_INFO(("deleting count: %d", delete_counter));
    }

    rc = zoo_delete(zh, path, -1);
    if (ZOK != rc) {
        LOG_ERROR(("Failed to delete znode %s, rc = %d", path, rc));
    }else {
        delete_counter++;
    }

    return rc;
}

static void _usage(char *argv[])
{
    fprintf(stderr, "USAGE:\t%s host path counter\n", argv[0]);
    fprintf(stderr, "\t%s host path clean\n", argv[0]);
    exit(0);
}

int main(int ac, char *av[])
{
    int node_count = 0;
    int cleaning = 0;

    if (ac < 4) {
        _usage(argv);
    }

    char *host = av[1];
    char *path = av[2];
    if (0 == strcmp("clean", av[3])) {
        cleaning = 1;
    } else {
        node_count = atoi(av[3]);
    }

    /* for test enable deterministic order */
    zoo_set_debug_level(ZOO_LOG_LEVEL_INFO);
    zoo_deterministic_conn_order(1);

    zh = zookeeper_init(host, _listener, 10000, NULL, NULL, 0);
    if (NULL == zh) {
        perror("zookeeper_init");
        exit(1);
    }

    LOG_INFO(("Checking server connection..."));
    _wait_connected();

    if (1 == cleaning) {
        int rc = 0;
        delete_counter = 0;

        rc = _recursive_delete(path);
        if (ZOK == rc) {
            LOG_INFO(("Succesfully deleted a subtree starting at %s (%d nodes)",
                path, delete_counter));
        }

        exit(rc);
    }

    _create_parent(path);
    int loop = 0;
    while(loop < 10) {
        _wait_connected();
        LOG_INFO(("Creating children for path %s", path);
        _do_create_nodes(path, node_count);
        _wait_counter();
        
        LOG_INFO(("Starting the write cycle for path %s", path));
        _do_writes(path, node_count);
        _wait_counter();

        LOG_INFO(("Starting the read cycle for path %s", path));
        _do_reads(path, node_count);
        _wait_counter();

        LOG_INFO(("Starting the delete cycle for path %s", path));
        _do_deletes(path, node_count);
        _wait_counter();
        loop++;
    }

    zookeeper_close(zh);
    return 0;
}
