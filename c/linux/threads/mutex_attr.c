#include "errors.h"
#include <pthread.h>

static pthread_mutex_t mutex;

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_mutexattr_t mutex_attr;

    status = pthread_mutexattr_init(&mutex_attr);
    if (0 != status) {
        err_abort(status, "Create attr");
    }

#ifdef _POSIX_THREAD_PROCESS_SHARED
    status = pthread_mutexattr_setpshared(
        &mutex_attr, PTHREAD_PROCESS_PRIVATE);
    if (0 != status) {
        err_abort(status, "Set pshared");
    }
#endif 

    status = pthread_mutex_init(&mutex, &mutex_attr);
    if (0 != status) {
        err_abort(status, "Init mutex");
    }

    return 0;
}
