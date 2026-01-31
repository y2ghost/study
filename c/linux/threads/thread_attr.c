#include "errors.h"
#include <limits.h>
#include <pthread.h>

static void *thread_routine(void *arg)
{
    printf("The thread is here\n");
    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    size_t stack_size = 0;
    pthread_t thread_id;
    pthread_attr_t thread_attr;

    status = pthread_attr_init(&thread_attr);
    if (0 != status) {
        err_abort(status, "Create attr");
    }

    status = pthread_attr_setdetachstate(&thread_attr, PTHREAD_CREATE_DETACHED);
    if (0 != status) {
        err_abort(status, "Set detach");
    }

#ifdef _POSIX_THREAD_ATTR_STACKSIZE
    /*
     * If supported, determine the default stack size and report
     * it, and then select a stack size for the new thread.
     *
     * Note that the standard does not specif y the default stack
     * size, and the default value in an attributes object need
     * not be the size that will actually be used.  Solaris 2.5
     * uses a value of 0 to indicate the default.
     */
    status = pthread_attr_getstacksize(&thread_attr, &stack_size);
    if (0 != status) {
        err_abort(status, "Get stack size");
    }

    printf("Default stack size is %u; minimum is %d\n",
       (unsigned)stack_size, PTHREAD_STACK_MIN);
    status = pthread_attr_setstacksize(
        &thread_attr, PTHREAD_STACK_MIN*2);

    if (0 != status) {
        err_abort(status, "Set stack size");
    }
#endif 

    status = pthread_create(
        &thread_id, &thread_attr, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    printf("Main exiting\n");
    pthread_exit(NULL);
    return 0;
}
