#include "barrier.h"
#include "errors.h"
#include <pthread.h>

int barrier_init(barrier_t *barrier, int count)
{
    int status = 0;

    barrier->cycle = 0;
    barrier->counter = count;
    barrier->threshold = count;
    status = pthread_mutex_init(&barrier->mutex, NULL);

    if (0 != status) {
        return status;
    }

    status = pthread_cond_init(&barrier->cv, NULL);
    if (0 != status) {
        pthread_mutex_destroy(&barrier->mutex);
        return status;
    }

    barrier->self = BARRIER_VALID;
    return 0;
}

int barrier_destroy(barrier_t *barrier)
{
    int status = 0;
    int status2 = 0;

    if (BARRIER_VALID != barrier->self) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&barrier->mutex);
    if (0 != status) {
        return status;
    }

    /* check wheter any threads are known to be waiting */
    if (barrier->counter != barrier->threshold) {
        pthread_mutex_unlock(&barrier->mutex);
        return EBUSY;
    }

    barrier->self = 0;
    status = pthread_mutex_unlock(&barrier->mutex);

    if (0 != status) {
        return status;
    }

    status = pthread_mutex_destroy(&barrier->mutex);
    status2 = pthread_cond_destroy(&barrier->cv);
    return(status == 0 ? status : status2);
}

int barrier_wait(barrier_t *barrier)
{
    int tmp = 0;
    int cycle = 0;
    int cancel = 0;
    int status = 0;

    if (BARRIER_VALID != barrier->self) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&barrier->mutex);
    if (0 != status) {
        return status;
    }

    /* remember which cycle we're on */
    cycle = barrier->cycle;
    barrier->counter--;

    if (0 == barrier->counter) {
        barrier->cycle = !barrier->cycle;
        barrier->counter = barrier->threshold;
        status = pthread_cond_broadcast(&barrier->cv);
        
        /* the last thread into the barrier will return status -1
         * then it do special serialc ode */
        if (0 == status) {
            status = -1;
        }
    } else {
        pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, &cancel);
        while (cycle == barrier->cycle) {
            status = pthread_cond_wait(&barrier->cv, &barrier->mutex);
            if (0 != status) {
                break;
            }
        }

        pthread_setcancelstate(cancel, &tmp);
    }
    
    pthread_mutex_unlock(&barrier->mutex);
    return status;
}
