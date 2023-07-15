#include "rwlock.h"
#include "errors.h"
#include <pthread.h>

#define THREADS     5
#define ITERATIONS  1000
#define DATASIZE    15

typedef struct thread_tag {
    int thread_num;
    pthread_t thread_id;
    int r_collisions;
    int w_collisions;
    int updates;
    int interval;
} thread_t;

typedef struct data_tag {
    rwlock_t lock;
    int data;
    int updates;
} data_t;

static thread_t threads[THREADS];
static data_t data[DATASIZE];

static void *thread_routine(void *arg)
{
    thread_t *self = (thread_t*)arg;
    int iteration = 0;
    int element = 0;
    int status = 0;

    element = 0;
    for (iteration=0; iteration<ITERATIONS; iteration++) {
        if (0 == (iteration%self->interval)) {
            status = rwl_writetrylock(&data[element].lock);
            if (status == EBUSY) {
                self->w_collisions++;
            } else if (status == 0) {
                data[element].data++;
                data[element].updates++;
                self->updates++;
                rwl_writeunlock(&data[element].lock);
            } else {
                err_abort(status, "Try write lock");
            }
        } else {
            status = rwl_readtrylock(&data[element].lock);
            if (status == EBUSY) {
                self->r_collisions++;
            } else if (0 != status) {
                err_abort(status, "Try read lock");
            } else {
                if (data[element].data != data[element].updates)
                    printf("%d: data[%d] %d != %d\n",
                        self->thread_num, element,
                        data[element].data, data[element].updates);
                rwl_readunlock(&data[element].lock);
            }
        }

        element++;
        if (element >= DATASIZE) {
            element = 0;
        }
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int count = 0;
    int data_count = 0;
    unsigned int seed = 1;
    int thread_updates = 0;
    int data_updates = 0;
    int status = 0;

    for (data_count=0; data_count<DATASIZE; data_count++) {
        data[data_count].data = 0;
        data[data_count].updates = 0;
        rwl_init(&data[data_count].lock);
    }

    for (count=0; count<THREADS; count++) {
        threads[count].thread_num = count;
        threads[count].r_collisions = 0;
        threads[count].w_collisions = 0;
        threads[count].updates = 0;
        threads[count].interval = rand_r(&seed) % ITERATIONS;
        status = pthread_create(&threads[count].thread_id,
            NULL, thread_routine,(void*)&threads[count]);

        if (0 != status) {
            err_abort(status, "Create thread");
        }
    }

    for (count=0; count<THREADS; count++) {
        status = pthread_join(threads[count].thread_id, NULL);
        if (0 != status) {
            err_abort(status, "Join thread");
        }

        thread_updates += threads[count].updates;
        printf("%02d: interval %d, updates %d, "
            "r_collisions %d, w_collisions %d\n",
            count, threads[count].interval,
            threads[count].updates,
            threads[count].r_collisions, threads[count].w_collisions);
    }

    for (data_count=0; data_count<DATASIZE; data_count++) {
        data_updates += data[data_count].updates;
        printf("data %02d: value %d, %d updates\n",
            data_count, data[data_count].data, data[data_count].updates);
        rwl_destroy(&data[data_count].lock);
    }

    return 0;
}
