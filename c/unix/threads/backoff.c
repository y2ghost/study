#include "errors.h"
#include <sched.h>
#include <pthread.h>

#define ITERATIONS  10

pthread_mutex_t mutex[3] = {
    PTHREAD_MUTEX_INITIALIZER,
    PTHREAD_MUTEX_INITIALIZER,
    PTHREAD_MUTEX_INITIALIZER,
};

typedef enum lock_order_tag {
    FORWARD,
    BACKWARD,
} lock_order;

/* whether to backoff or deadlock */
static int backoff = 1;
/* 0: no yield, >0: yield, <0: sleep */
static int yield_flag = 0;

static void _lock(lock_order order)
{
    int i = 0;
    int status = 0;
    int backoffs = 0;
    char *lock_type = NULL;
    pthread_mutex_t *mutex_ptrs[3] = {NULL};

    switch (order) {
    case FORWARD:
        mutex_ptrs[0] = &mutex[0];
        mutex_ptrs[1] = &mutex[1];
        mutex_ptrs[2] = &mutex[2];
        lock_type = "forward";
        break;
    case BACKWARD:
        mutex_ptrs[0] = &mutex[2];
        mutex_ptrs[1] = &mutex[1];
        mutex_ptrs[2] = &mutex[0];
        lock_type = "backward";
        break;
    default:
        fprintf(stderr, "unkown order, cannot happen!\n");
        exit(1);
    }

    backoffs = 0;
    for (i=0; i<3; i++) {
        if (0 == i) {
            status = pthread_mutex_lock(mutex_ptrs[i]);
            if (0 != status) {
                err_abort(status, "First lock");
            }
        } else {
            if (0 != backoff) {
                status = pthread_mutex_trylock(mutex_ptrs[i]);
            } else {
                status = pthread_mutex_lock(mutex_ptrs[i]);
            }
    
            if (EBUSY == status) {
                backoffs++;
                DPRINTF((" [%s locker backing off at %d]\n", lock_type, i));

                for(; i>=0; i--) {
                    status = pthread_mutex_unlock(mutex_ptrs[i]);
                    if (0 != status) {
                        err_abort(status, "Backoff");
                    }
                }
            } else {
                if (0 != status) {
                    err_abort(status, "Lock mutex");
                }

                DPRINTF((" %s locker got %d\n", lock_type, i));
            }
        }

        /*
         * Yield processor, if  needed to be sure locks get
         * interleaved on a uniprocessor.
         */
        if (yield_flag) {
            if (yield_flag > 0) {
                sched_yield();
            } else {
                sleep(1);
            }
        }           
    }

    printf("lock %s got all locks, %d backoffs\n", lock_type, backoffs);
    pthread_mutex_unlock(mutex_ptrs[0]);
    pthread_mutex_unlock(mutex_ptrs[1]);
    pthread_mutex_unlock(mutex_ptrs[2]);
    sched_yield();
}

static void *lock_forward(void *arg)
{
    int iterate = 0;

    for (iterate=0; iterate<ITERATIONS; iterate++) {
        _lock(FORWARD);
    }

    return NULL;
}

static void *lock_backward(void *arg)
{
    int iterate = 0;

    for (iterate=0; iterate<ITERATIONS; iterate++) {
        _lock(BACKWARD);
    }
    
    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t forward;
    pthread_t backward;

    if (argc > 1) {
        backoff = atoi(argv[1]);
    }

    if (argc > 2) {
        yield_flag = atoi(argv[2]);
    }

    status = pthread_create(&forward, NULL, lock_forward, NULL);
    if (0 != status) {
        err_abort(status, "Create forward");
    }

    status = pthread_create(&backward, NULL, lock_backward, NULL);
    if (0 != status) {
        err_abort(status, "Create backward");  
    }

    pthread_exit(NULL);
}
