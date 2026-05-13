#include "errors.h"
#include <pthread.h>
#include <sys/wait.h>
#include <sys/types.h>

static pid_t self_pid = 0;
static pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

static void fork_prepare(void)
{
    int status = 0;

    status = pthread_mutex_lock(&mutex);
    if (0 != status) {
        err_abort(status, "Lock in prepare handler");
    }
}

static void fork_parent(void)
{
    int status = 0;

    status = pthread_mutex_unlock(&mutex);
    if (0 != status) {
        err_abort(status, "Unlock in parent handler");
    }
}

static void fork_child(void)
{
    int status = 0;

    self_pid = getpid();
    status = pthread_mutex_unlock(&mutex);

    if (0 != status) {
        err_abort(status, "Unlock in child handler");
    }
}

static void *thread_routine(void *arg)
{
    int status = 0;
    pid_t child_pid = 0;

    child_pid = fork();
    if (-1 == child_pid) {
        errno_abort("Fork");
    }

    /*
     * Lock the mutex -- without the atfork handlers, the mutex will remain
     * locked in the child process and this lock attempt will hang(or fail
     * with EDEADLK) in the child.
     */
    status = pthread_mutex_lock(&mutex);
    if (0 != status) {
        err_abort(status, "Lock in child");
    }

    status = pthread_mutex_unlock(&mutex);
    if (0 != status) {
        err_abort(status, "Unlock in child");
    }

    printf("After fork: %d(%d)\n", child_pid, self_pid);
    if (0 != child_pid) {
        if (-1 == waitpid(child_pid,(int*)0, 0)) {
            errno_abort("Wait for child");
        }
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    int atfork_flag = 1;
    pthread_t fork_thread;

    if (argc > 1) {
        atfork_flag = atoi(argv[1]);
    }

    if (atfork_flag) {
        status = pthread_atfork(fork_prepare, fork_parent, fork_child);
        if (0 != status) {
            err_abort(status, "Register fork handlers");
        }
    }

    self_pid = getpid();
    status = pthread_mutex_lock(&mutex);

    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    /*
     * Create a thread while  the mutex is locked. It will fork a process,
     * which(without atfork handlers) will run with the mutex locked.
     */
    status = pthread_create(&fork_thread, NULL, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    sleep(5);
    status = pthread_mutex_unlock(&mutex);

    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }

    status = pthread_join(fork_thread, NULL);
    if (0 != status) {
        err_abort(status, "Join thread");
    }

    return 0;
}
