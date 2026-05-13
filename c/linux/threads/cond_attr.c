#include "errors.h"
#include <pthread.h>

static pthread_cond_t cond;

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_condattr_t cond_attr;

    status = pthread_condattr_init(&cond_attr);
    if (0 != status) {
        err_abort(status, "Create attr");
    }

#ifdef _POSIX_THREAD_PROCESS_SHARED
    status = pthread_condattr_setpshared(
        &cond_attr, PTHREAD_PROCESS_PRIVATE);
    if (0 != status) {
        err_abort(status, "Set pshared");
    }
#endif 

    status = pthread_cond_init(&cond, &cond_attr);
    if (0 != status) {
        err_abort(status, "Init cond");
    }

    return 0;
}
