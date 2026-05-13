#include "errors.h"
#include <pthread.h>
#include <sys/time.h>
#include <sys/signal.h>

static int counter = 0;
static timer_t timer_id;
static pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

static void timer_thread(union sigval val)
{
    int status = 0;

    status = pthread_mutex_lock(&mutex);
    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    counter++;
    if (counter >= 5) {
        status = pthread_cond_signal(&cond);
        if (0 != status) {
            err_abort(status, "Signal condition");
        }
    }

    status = pthread_mutex_unlock(&mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    printf("Timer %d\n", counter);
}

int main(void)
{
    int status = 0;
    struct sigevent se;
    struct itimerspec ts;
    
    se.sigev_notify = SIGEV_THREAD;
    se.sigev_value.sival_ptr = &timer_id;
    se.sigev_notify_function = timer_thread;
    se.sigev_notify_attributes = NULL;

    ts.it_value.tv_sec = 5;
    ts.it_value.tv_nsec = 0;
    ts.it_interval.tv_sec = 5;
    ts.it_interval.tv_nsec = 0;

    DPRINTF(("Creating timer\n"));
    status = timer_create(CLOCK_REALTIME, &se, &timer_id);

    if (status == -1) {
        errno_abort("Create timer");
    }

    DPRINTF(("Setting timer %ld for 5-second expiration...\n", (long)timer_id));
    status = timer_settime(timer_id, 0, &ts, 0);

    if (-1 == status) {
        errno_abort("Set timer");
    }

    status = pthread_mutex_lock(&mutex);
    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    while (counter < 5) {
        status = pthread_cond_wait(&cond, &mutex);
        if (0 != status) {
            err_abort(status, "Wait on condition");
        }
    }

    status = pthread_mutex_unlock(&mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    return 0;
}
