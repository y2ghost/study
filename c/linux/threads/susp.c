#include "errors.h"
#include <pthread.h>
#include <sched.h>
#include <signal.h>

#define THREAD_COUNT    20
#define ITERATIONS      40000

static int bottom = 0;
static int inited = 0;
static pthread_t *array = NULL;
static volatile int sentinel = 0;
static pthread_t null_pthread = {0};
static unsigned long iterations = ITERATIONS;
static pthread_once_t once = PTHREAD_ONCE_INIT;
static pthread_mutex_t mut = PTHREAD_MUTEX_INITIALIZER;

static void suspend_signal_handler(int sig)
{
    sigset_t signal_set;

    sigfillset(&signal_set);
    sigdelset(&signal_set, SIGUSR2);
    sentinel = 1;
    sigsuspend(&signal_set);
}

static void resume_signal_handler(int sig)
{
    return; /* DO NOTHING */
}

static void suspend_init_routine(void)
{
    int status = 0;
    struct sigaction sigusr1;
    struct sigaction sigusr2;

    bottom = 10;
    array = (pthread_t*)calloc(bottom, sizeof(*array));

    sigusr1.sa_flags = 0;
    sigusr1.sa_handler = suspend_signal_handler;
    sigemptyset(&sigusr1.sa_mask);

    sigusr2.sa_flags = 0;
    sigusr2.sa_handler = resume_signal_handler;
    sigusr2.sa_mask = sigusr1.sa_mask;

    status = sigaction(SIGUSR1, &sigusr1, NULL);
    if (status == -1) {
        errno_abort("Installing suspend handler");
    }
    
    status = sigaction(SIGUSR2, &sigusr2, NULL);
    if (status == -1) {
        errno_abort("Installing resume handler");
    }
    
    inited = 1;
    return;
}

static int thd_suspend(pthread_t target_thread)
{
    int i = 0;
    int status = 0;

    status = pthread_once(&once, suspend_init_routine);
    if (0 != status) {
        return status;
    }

    status = pthread_mutex_lock(&mut);
    if (0 != status) {
        return status;
    }

    while (i < bottom) 
        if (array[i++] == target_thread) {
            status = pthread_mutex_unlock(&mut);
            return status;
        }

    i = 0;
    while (array[i] != 0) {
        i++;
    }

    if (i == bottom) {
        array = (pthread_t*)realloc(array,(++bottom * sizeof(*array)));
        if (NULL == array) {
            pthread_mutex_unlock(&mut);
            return errno;
        }

        array[bottom] = null_pthread;
    }

    sentinel = 0;
    status = pthread_kill(target_thread, SIGUSR1);

    if (0 != status) {
        pthread_mutex_unlock(&mut);
        return status;
    }

    while (0 == sentinel) {
        sched_yield();
    }
    
    array[i] = target_thread;
    status = pthread_mutex_unlock(&mut);
    return status;
}

static int thd_continue(pthread_t target_thread)
{
    int i = 0;
    int status = 0;

    status = pthread_mutex_lock(&mut);
    if (0 != status) {
        return status;
    }

    if (0 == inited) {
        status = pthread_mutex_unlock(&mut);
        return status;
    }

    while (array[i]!=target_thread && i<bottom) {
        i++;
    }

    if (i >= bottom) {
        pthread_mutex_unlock(&mut);
        return 0;
    }

    status = pthread_kill(target_thread, SIGUSR2);
    if (0 != status) {
        pthread_mutex_unlock(&mut);
        return status;
    }

    array[i] = 0;
    status = pthread_mutex_unlock(&mut);
    return status;
}

static void *thread_routine(void *arg)
{
    int i = 0;
    int number = *(int*)arg;
    char buffer[128] = {'\0'};

    for (i=1; i<=iterations; i++) {
        if (0 == i%2000) {
            sprintf(buffer, "Thread %02d: %d\n", number, i);
            write(1, buffer, strlen(buffer));
        }

        sched_yield();
    }

    return(void *)0;
}

int main(int argc, char *argv[])
{
    int i = 0;
    int status = 0;
    pthread_attr_t detach;
    pthread_t threads[THREAD_COUNT];

    status = pthread_attr_init(&detach);
    if (0 != status) {
        err_abort(status, "Init attributes object");
    }

    status = pthread_attr_setdetachstate(&detach, PTHREAD_CREATE_DETACHED);
    if (0 != status) {
        err_abort(status, "Set create-detached");
    }

    for (i=0; i<THREAD_COUNT; i++) {
        status = pthread_create(&threads[i], &detach, thread_routine, &i);
        if (0 != status) {
            err_abort(status, "Create thread");
        }
    }

    sleep(2);
    for (i=0; i<THREAD_COUNT/2; i++) {
        printf("Suspending thread %d.\n", i);
        status = thd_suspend(threads[i]);

        if (0 != status) {
            err_abort(status, "Suspend thread");
        }
    }

    printf("Sleeping ...\n");
    sleep(2);

    for (i=0; i<THREAD_COUNT/2; i++) {
        printf("Continuing thread %d.\n", i);
        status = thd_continue(threads[i]);

        if (0 != status) {
            err_abort(status, "Suspend thread");
        }
    }

    for (i=THREAD_COUNT/2; i<THREAD_COUNT; i++) {
        printf("Suspending thread %d.\n", i);
        status = thd_suspend(threads[i]);

        if (0 != status) {
            err_abort(status, "Suspend thread");
        }
    }

    printf("Sleeping ...\n");
    sleep(2);

    for (i=THREAD_COUNT/2; i<THREAD_COUNT; i++) {
        printf("Continuing thread %d.\n", i);
        status = thd_continue(threads[i]);

        if (0 != status) {
            err_abort(status, "Continue thread");
        }
    }

    pthread_exit(NULL);
}
