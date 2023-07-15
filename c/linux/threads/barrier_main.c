#include "barrier.h"
#include "errors.h"
#include <pthread.h>

#define THREADS     5
#define ARRAY       6
#define INLOOPS     1000
#define OUTLOOPS    10

typedef struct thread_tag {
    int number;
    int increment;
    int array[ARRAY];
    pthread_t thread_id;
} thread_t;

static barrier_t barrier;
static thread_t thread[THREADS];

static void *thread_routine(void *arg)
{
    int count = 0;
    int status = 0;
    int in_loop = 0;
    int out_loop = 0;
    thread_t *self = (thread_t*)arg;
    
    for (out_loop=0; out_loop<OUTLOOPS; out_loop++) {
        status = barrier_wait(&barrier);
        if (status > 0) {
            err_abort(status, "Wait on barrier");
        }

        for (in_loop=0; in_loop<INLOOPS; in_loop++) {
            for (count=0; count<ARRAY; count++) {
                self->array[count] += self->increment;
            }
        }

        status = barrier_wait(&barrier);
        if (status > 0) {
            err_abort(status, "Wait on barrier");
        }

        if (-1 == status) {
            int thread_num = 0;
            for (thread_num=0; thread_num<THREADS; thread_num++) {
                thread[thread_num].increment += 1;
            }
        }
    }

    return NULL;
}

int main(int arg, char *argv[])
{
    int status = 0;
    int array_count = 0;
    int thread_count = 0;
    thread_t *temp_thread = NULL;

    barrier_init(&barrier, THREADS);
    for (thread_count=0; thread_count<THREADS; thread_count++) {
        temp_thread = &thread[thread_count];
        temp_thread->number = thread_count;
        temp_thread->increment = thread_count;

        for (array_count=0; array_count<ARRAY; array_count++) {
            temp_thread->array[array_count] = array_count + 1;
        }

        status = pthread_create(&temp_thread->thread_id, NULL,
            thread_routine,(void*)temp_thread);
        if (0 != status) {
            err_abort(status, "Create thread");
        }
    }

    for (thread_count=0; thread_count<THREADS; thread_count++) {
        temp_thread = &thread[thread_count];
        status = pthread_join(temp_thread->thread_id, NULL);
        if (0 != status) {
            err_abort(status, "Join thread");
        }

        printf("%02d:(%d) ", thread_count, temp_thread->increment);
        for (array_count=0; array_count<ARRAY; array_count++) {
            printf("%010u ", temp_thread->array[array_count]);
        }

        printf("\n");
    }

    barrier_destroy(&barrier);
    return 0;
}
