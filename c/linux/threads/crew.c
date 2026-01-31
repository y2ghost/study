#include "errors.h"
#include <dirent.h>
#include <pthread.h>
#include <sys/stat.h>
#include <sys/types.h>

#define CREW_SIZE 4

typedef struct work_tag work_t;
typedef struct work_tag *work_p;
typedef struct worker_tag worker_t;
typedef struct worker_tag *worker_p;
typedef struct crew_tag crew_t;
typedef struct crew_tag *crew_p;
typedef int (*handler_t)(worker_p worker, work_p work);

struct work_tag {
    char *path;
    char *string;
    struct work_tag *next;
};

struct worker_tag {
    int index;
    pthread_t thread;
    struct crew_tag *crew;
};

struct crew_tag {
    int crew_size;
    long work_count;
    work_t *first;
    work_t *last;
    worker_t crew[CREW_SIZE];
    pthread_mutex_t mutex;
    pthread_cond_t done;
    pthread_cond_t go;
};

static size_t path_max = 0;
static size_t name_max = 0;

static int _process_link_file(worker_p worker, work_p work)
{
    printf("Thread %d: %s is a link, skipping.\n", worker->index, work->path);
    return 0;
}

static int _process_dir_file(worker_p worker, work_p work)
{
    int status = 0;
    DIR *directory = NULL;
    work_p new_work = NULL;
    crew_p crew = worker->crew;
    struct dirent *entry = NULL;

    entry = (struct dirent*)malloc(sizeof(*entry) + name_max);
    if (NULL == entry) {
        errno_abort("Allocating dirent");
    }

    directory = opendir(work->path);
    if (NULL == directory) {
        fprintf(stderr, "Unable to open directory %s: %d(%s)\n",
            work->path,
            errno, strerror(errno));
        return 1;
    }
    
    while (1) {
        entry  = readdir(directory);
        if (NULL == entry) {
            break;
        }
        
        if (0 == strcmp(entry->d_name,".")) {
            continue;
        }
    
        if (0 == strcmp(entry->d_name,"..")) {
            continue;
        }
    
        new_work = (work_p)malloc(sizeof(*new_work));
        if (NULL == new_work) {
            errno_abort("Unable to allocate space");
        }
    
        new_work->path = (char*)malloc(path_max);
        if (NULL == new_work->path) {
            errno_abort("Unable to allocate path");
        }
    
        snprintf(new_work->path, path_max, "%s/%s", work->path, entry->d_name);
        new_work->string = work->string;
        new_work->next = NULL;
        status = pthread_mutex_lock(&crew->mutex);
    
        if (0 != status) {
            err_abort(status, "Lock mutex");
        }
    
        if (NULL == crew->first) {
            crew->first = new_work;
            crew->last = new_work;
        } else {
            crew->last->next = new_work;
            crew->last = new_work;
        }
    
        crew->work_count++;
        DPRINTF(("Crew %d: add work %p, first %p, last %p, %ld\n",
            worker->index, new_work, crew->first,
            crew->last, crew->work_count));
        status = pthread_cond_signal(&crew->go);
        status = pthread_mutex_unlock(&crew->mutex);
    
        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }
    }
    
    closedir(directory);
    free(entry);
    return 0;
}

static int _process_reg_file(worker_p worker, work_p work)
{
    FILE *search = NULL;
    char *bufptr = NULL;
    char *search_ptr = NULL;
    char buffer[256] = {'\0'};
    
    search = fopen(work->path, "r");
    if (NULL == search) {
        fprintf(stderr, "Unable to open %s: %d(%s)\n",
            work->path,
            errno, strerror(errno));
        return 1;
    }

    while (1) {
        bufptr = fgets(buffer, sizeof(buffer), search);
        if (NULL == bufptr) {
            if (feof(search)) {
                break;
            }
    
            if (ferror(search)) {
                fprintf(stderr, "Unable to read %s: %d(%s)\n",
                    work->path,
                    errno, strerror(errno));
                break;
            }
        }
    
        search_ptr = strstr(buffer, work->string);
        if (NULL != search_ptr) {
            flockfile(stdout);
            printf("Thread %d found \"%s\" in %s\n",
                worker->index, work->string, work->path);
            funlockfile(stdout);
            break;
        }
    }
    
    fclose(search);
    return 0;
}

static int _process_none_file(worker_p worker, work_p work)
{
    struct stat filestat;
    char *file_info = NULL;

    lstat(work->path, &filestat);
    if (S_ISFIFO(filestat.st_mode)) {
        file_info = "FIFO";
    } else if (S_ISCHR(filestat.st_mode)) {
        file_info = "CHR";
    } else if (S_ISBLK(filestat.st_mode)) {
        file_info = "BLK";
    } else if (S_ISSOCK(filestat.st_mode)) {
        file_info = "SOCK";
    } else {
        file_info = "UNKNOWN";
    }

    fprintf(stderr, "Thread %d: %s is type %o(%s))\n",
        worker->index,
        work->path,
        filestat.st_mode & S_IFMT,
        file_info);
    return 0;
}

static void *worker_routine(void *arg)
{
    int status = 0;
    work_p work = NULL;
    struct stat filestat;
    handler_t handler = NULL;
    worker_p worker = (worker_t*)arg;
    crew_p crew = worker->crew;
    
    status = pthread_mutex_lock(&crew->mutex);
    if (0 != status) {
        err_abort(status, "Lock crew mutex");
    }

    while (0 == crew->work_count) {
        status = pthread_cond_wait(&crew->go, &crew->mutex);
        if (0 != status) {
            err_abort(status, "Wait for go");
        }
    }

    status = pthread_mutex_unlock(&crew->mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    DPRINTF(("Crew %d starting\n", worker->index));
    while (1) {
        status = pthread_mutex_lock(&crew->mutex);
        if (0 != status) {
            err_abort(status, "Lock crew mutex");
        }

        DPRINTF(("Crew %d top: first is %p, count is %ld\n",
            worker->index, crew->first, crew->work_count));
        while (NULL == crew->first) {
            status = pthread_cond_wait(&crew->go, &crew->mutex);
            if (0 != status) {
                err_abort(status, "Wait for work");
            }
        }

        DPRINTF(("Crew %d woke: %p, %ld\n",
            worker->index, crew->first, crew->work_count));
        work = crew->first;
        crew->first = work->next;

        if (NULL == crew->first) {
            crew->last = NULL;
        }

        DPRINTF(("Crew %d took %p, leaves first %p, last %p\n",
            worker->index, work, crew->first, crew->last));
        status = pthread_mutex_unlock(&crew->mutex);

        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }

        status = lstat(work->path, &filestat);
        if (S_ISLNK(filestat.st_mode)) {
            handler = _process_link_file;
        } else if (S_ISDIR(filestat.st_mode)) {
            handler = _process_dir_file;
        } else if (S_ISREG(filestat.st_mode)) {
            handler = _process_reg_file;
        }else {
            handler = _process_none_file;
        }

        handler(worker, work);
        free(work->path);
        free(work);

        status = pthread_mutex_lock(&crew->mutex);
        if (0 != status) {
            err_abort(status, "Lock crew mutex");
        }

        crew->work_count--;
        DPRINTF(("Crew %d decremented work to %ld\n",
            worker->index, crew->work_count));

        if (crew->work_count <= 0) {
            DPRINTF(("Crew thread %d done\n", worker->index));
            status = pthread_cond_broadcast(&crew->done);

            if (0 != status) {
                err_abort(status, "Wake waiters");
            }

            status = pthread_mutex_unlock(&crew->mutex);
            if (0 != status) {
                err_abort(status, "Unlock mutex");
            }

            break;
        }

        status = pthread_mutex_unlock(&crew->mutex);
        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }
    }

    return NULL;
}

static int crew_create(crew_p crew, int crew_size)
{
    int status = 0;
    int crew_index = 0;
    worker_p worker = NULL;

    if (crew_size > CREW_SIZE) {
        return EINVAL;
    }

    crew->crew_size = crew_size;
    crew->work_count = 0;
    crew->first = NULL;
    crew->last = NULL;

    status = pthread_mutex_init(&crew->mutex, NULL);
    if (0 != status) {
        return status;
    }

    status = pthread_cond_init(&crew->done, NULL);
    if (0 != status) {
        return status;
    }

    status = pthread_cond_init(&crew->go, NULL);
    if (0 != status) {
        return status;
    }

    for (crew_index=0; crew_index<crew_size; ++crew_index) {
        worker = &crew->crew[crew_index];
        worker->index = crew_index;
        worker->crew = crew;
        status = pthread_create(&worker->thread, NULL,
            worker_routine, (void*)worker);

        if (0 != status) {
            err_abort(status, "Create worker");
        }
    }

    return 0;
}

static int crew_start(crew_p crew, char *filepath, char *search)
{
    int status = 0;
    work_p start_work = NULL;

    status = pthread_mutex_lock(&crew->mutex);
    if (0 != status) {
        return status;
    }

    while (crew->work_count > 0) {
        status = pthread_cond_wait(&crew->done, &crew->mutex);
        if (0 != status) {
            pthread_mutex_unlock(&crew->mutex);
            return status;
        }
    }

    errno = 0;
    path_max = pathconf(filepath, _PC_PATH_MAX);

    if (-1 == path_max) {
        if (0 != errno) {
            errno_abort("Unable to get PATH_MAX");
        }

        path_max = 1024;
    }

    errno = 0;
    name_max = pathconf(filepath, _PC_NAME_MAX);

    if (-1 == name_max) {
        if (0 != errno) {
            errno_abort("Unable to get NAME_MAX");
        }

        name_max = 256;
    }

    DPRINTF(("PATH_MAX for %s is %ld, NAME_MAX is %ld\n",
        filepath, path_max, name_max));
    path_max++;
    name_max++;
    start_work = (work_p)malloc(sizeof(*start_work));

    if (NULL == start_work) {
        errno_abort("Unable to allocate start_work");
    }

    DPRINTF(("filepath: %s\n", filepath));
    start_work->path = (char*)malloc(path_max);

    if (NULL == start_work->path) {
        errno_abort("Unable to allocate path");
    }

    snprintf(start_work->path, path_max, "%s", filepath);
    start_work->string = search;
    start_work->next = NULL;

    if (NULL == crew->first) {
        crew->first = start_work;
        crew->last = start_work;
    } else {
        crew->last->next = start_work;
        crew->last = start_work;
    }

    crew->work_count++;
    status = pthread_cond_signal(&crew->go);

    if (0 != status) {
        free(crew->first);
        crew->first = NULL;
        crew->work_count = 0;
        pthread_mutex_unlock(&crew->mutex);
        return status;
    }

    while (crew->work_count > 0) {
        status = pthread_cond_wait(&crew->done, &crew->mutex);
        if (0 != status) {
            err_abort(status, "waiting for crew to finish");
        }
    }

    status = pthread_mutex_unlock(&crew->mutex);
    if (0 != status) {
        err_abort(status, "Unlock crew mutex");
    }

    return 0;
}

int main(int argc, char *argv[])
{
    int status = 0;
    crew_t my_crew;

    if (argc < 3) {
        fprintf(stderr, "Usage: %s string path\n", argv[0]);
        return -1;
    }

    status = crew_create(&my_crew, CREW_SIZE);
    if (0 != status) {
        err_abort(status, "Create crew");
    }

    status = crew_start(&my_crew, argv[2], argv[1]);
    if (0 != status) {
        err_abort(status, "Start crew");
    }

    return 0;
}
