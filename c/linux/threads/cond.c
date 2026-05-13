#include "errors.h"
#include <time.h>
#include <pthread.h>

typedef struct my_struct_tag {
    pthread_mutex_t mutex;
    pthread_cond_t cond;
    int value;
} my_struct_t;

static int hibernation = 1;
static my_struct_t data = {PTHREAD_MUTEX_INITIALIZER, PTHREAD_COND_INITIALIZER, 0};

static void *wait_thread(void *arg)
{
    int status = 0;

    sleep(hibernation);
    status = pthread_mutex_lock(&data.mutex);

    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    data.value = 1;
    status = pthread_cond_signal(&data.cond);

    if (0 != status) {
        err_abort(status, "Signal condition");
    }

    status = pthread_mutex_unlock(&data.mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    struct timespec timeout;
    pthread_t wait_thread_id;

    if (argc > 1) {
        hibernation = atoi(argv[1]);
    }

    status = pthread_create(&wait_thread_id, NULL, wait_thread, NULL);
    if (0 != status) {
        err_abort(status, "Create wait thread");
    }


    timeout.tv_sec = time(NULL) + 2;
    timeout.tv_nsec = 0;
    status = pthread_mutex_lock(&data.mutex);

    if (0 != status) {
        err_abort(status, "Lock mutex");
    }


    while (0 == data.value) {
        status = pthread_cond_timedwait(&data.cond, &data.mutex, &timeout);
        if (ETIMEDOUT == status) {
            printf("Condition wait timed out.\n");
            break;
        } else if (0 != status) {
            err_abort(status, "Wait on condition");
        }
    }

    if (0 != data.value) {
        printf("Condition was signaled.\n");
    }

    status = pthread_mutex_unlock(&data.mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    return 0;
}
