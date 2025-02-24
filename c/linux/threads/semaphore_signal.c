#include "errors.h"
#include <sys/types.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <signal.h>
#include <time.h>

static sem_t semaphore;

static void signal_catcher(int sig)
{
    if (-1 == sem_post(&semaphore)) {
        errno_abort("Post semaphore");
    }
}

static void *sem_waiter(void *arg)
{
    int counter = 0;
    int number = (int)arg;

    for (counter=1; counter<=5; counter++) {
        while (-1 == sem_wait(&semaphore)) {
            if (errno != EINTR) {
                errno_abort("Wait on semaphore");
            }
        }

        printf("%d waking(%d)...\n", number, counter);
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int thread_count = 0;
    int status = 0;
    struct sigevent sig_event;
    struct sigaction sig_action;
    sigset_t sig_mask;
    timer_t timer_id;
    struct itimerspec timer_val;
    pthread_t sem_waiters[5];

#if  !defined(_POSIX_SEMAPHORES) || !defined(_POSIX_TIMERS)
# if  !defined(_POSIX_SEMAPHORES)
    printf("This system does not support POSIX semaphores\n");
# endif 
# if  !defined(_POSIX_TIMERS)
    printf("This system does not support POSIX timers\n");
# endif 
    return -1;
#else
    sem_init(&semaphore, 0, 0);

    for (thread_count=0; thread_count<5; thread_count++) {
        status = pthread_create(&sem_waiters[thread_count], NULL,
                sem_waiter, (void*)thread_count);
        if (0 != status) {
            err_abort(status, "Create thread");
        }
    }

    sig_event.sigev_value.sival_int = 0;
    sig_event.sigev_signo = SIGRTMIN;
    sig_event.sigev_notify = SIGEV_SIGNAL;

    if (-1 == timer_create(CLOCK_REALTIME, &sig_event, &timer_id)) {
        errno_abort("Create timer");
    }

    sigemptyset(&sig_mask);
    sigaddset(&sig_mask, SIGRTMIN);
    sig_action.sa_handler = signal_catcher;
    sig_action.sa_mask = sig_mask;
    sig_action.sa_flags = 0;

    if (-1 == sigaction(SIGRTMIN, &sig_action, NULL)) {
        errno_abort("Set signal action");
    }

    timer_val.it_interval.tv_sec = 2;
    timer_val.it_interval.tv_nsec = 0;
    timer_val.it_value.tv_sec = 2;
    timer_val.it_value.tv_nsec = 0;

    if (-1 == timer_settime(timer_id, 0, &timer_val, NULL)) {
        errno_abort("Set timer");
    }

    for (thread_count=0; thread_count<5; thread_count++) {
        status = pthread_join(sem_waiters[thread_count], NULL);
        if (0 != status) {
            err_abort(status, "Join thread");
        }
    }

    return 0;
#endif 
}
