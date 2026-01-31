#include "errors.h"
#include <pthread.h>

static pthread_mutex_t mutex;
static pthread_once_t once_block = PTHREAD_ONCE_INIT;

static void once_init_routine(void)
{
    int status = 0;

    status = pthread_mutex_init(&mutex, NULL);
    if (0 != status) {
        err_abort(status, "Init Mutex");
    }
}

static void *thread_routine(void *arg)
{
    int status = 0;

    status = pthread_once(&once_block, once_init_routine);
    if (0 != status) {
        err_abort(status, "Once init");
    }

    status = pthread_mutex_lock(&mutex);
    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    printf("thread_routine has locked the mutex.\n");
    status = pthread_mutex_unlock(&mutex);

    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t thread_id;

    status = pthread_create(&thread_id, NULL, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    status = pthread_once(&once_block, once_init_routine);
    if (0 != status) {
        err_abort(status, "Once init");
    }

    status = pthread_mutex_lock(&mutex);
    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    printf("Main has locked the mutex.\n");
    status = pthread_mutex_unlock(&mutex);

    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    status = pthread_join(thread_id, NULL);
    if (0 != status) {
        err_abort(status, "Join thread");
    }

    return 0;
}
