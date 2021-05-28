#include "errors.h"
#include <sys/types.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>

static int interrupted = 0;
static sigset_t signal_set;
static pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
static pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

static void *signal_waiter(void *arg)
{
    int status = 0;
    int sig_number = 0;
    int signal_count = 0;

    while (1) {
        sigwait(&signal_set, &sig_number);
        if (SIGINT != sig_number) {
            continue;
        }

        signal_count++;
        printf("Got SIGINT(%d of 5)\n", signal_count);

        if (signal_count < 5) {
            continue;
        }

        status = pthread_mutex_lock(&mutex);
        if (0 != status) {
            err_abort(status, "Lock mutex");
        }

        interrupted = 1;
        status = pthread_cond_signal(&cond);

        if (0 != status) {
            err_abort(status, "Signal condition");
        }

        status = pthread_mutex_unlock(&mutex);
        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }

        break;
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t signal_thread_id;

    sigemptyset(&signal_set);
    sigaddset(&signal_set, SIGINT);

    status = pthread_sigmask(SIG_BLOCK, &signal_set, NULL);
    if (0 != status) {
        err_abort(status, "Set signal mask");
    }

    status = pthread_create(&signal_thread_id, NULL,
        signal_waiter, NULL);
    if (0 != status) {
        err_abort(status, "Create sigwaiter");
    }

    status = pthread_mutex_lock(&mutex);
    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    while (0 == interrupted) {
        status = pthread_cond_wait(&cond, &mutex);
        if (0 != status) {
            err_abort(status, "Wait for interrupt");
        }
    }

    status = pthread_mutex_unlock(&mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    printf("Main terminating with SIGINT\n");
    return 0;
}
