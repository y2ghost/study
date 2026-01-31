#include "errors.h"
#include <pthread.h>

#define THREADS 5

typedef struct control_tag {
    int busy;
    int counter;
    pthread_cond_t cv;
    pthread_mutex_t mutex;
} control_t;

control_t control = {
    1, 0, PTHREAD_COND_INITIALIZER, PTHREAD_MUTEX_INITIALIZER,
};

static void cleanup_handler(void *arg)
{
    int status = 0;
    control_t *st = (control_t *)arg;

    st->counter--;
    printf("cleanup_handler: counter == %d\n", st->counter);
    status = pthread_mutex_unlock(&st->mutex);

    if (0 != status) {
        err_abort(status, "Unlock in cleanup handler");
    }
}

static void *thread_routine(void *arg)
{
    int status = 0;

    pthread_cleanup_push(cleanup_handler,(void*)&control);
    status = pthread_mutex_lock(&control.mutex);

    if (0 != status) {
        err_abort(status, "Mutex lock");
    }

    control.counter++;
    while (0 != control.busy) {
        status = pthread_cond_wait(&control.cv, &control.mutex);
        if (0 != status) {
            err_abort(status, "Wait on condition");
        }
    }

    pthread_cleanup_pop(1);
    return NULL;
}

int main(int argc, char *argv[])
{
    int count = 0;
    int status = 0;
    void *result = NULL;
    pthread_t thread_id[THREADS];

    for (count=0; count<THREADS; count++) {
        status = pthread_create(&thread_id[count], NULL, thread_routine, NULL);
        if (0 != status) {
            err_abort(status, "Create thread");
        }
    }

    sleep(2);
    for (count=0; count<THREADS; count++) {
        status = pthread_cancel(thread_id[count]);
        if (0 != status) {
            err_abort(status, "Cancel thread");
        }

        status = pthread_join(thread_id[count], &result);
        if (0 != status) {
            err_abort(status, "Join thread");
        }

        if (PTHREAD_CANCELED == result) {
            printf("thread %d cancelled\n", count);
        } else {
            printf("thread %d was not cancelled\n", count);
        }
    }

    return 0;    
}
