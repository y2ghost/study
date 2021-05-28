#include "errors.h"
#include <pthread.h>

static void *thread_routine(void *arg)
{
    return arg;
}

int main(int argc, char *argv[])
{
    pthread_t thread_id;
    void *thread_result = NULL;
    int status = 0;

    status = pthread_create(&thread_id, NULL, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    status = pthread_join(thread_id, &thread_result);
    if (0 != status) {
        err_abort(status, "Join thread");
    }

    if (NULL == thread_result) {
        return 0;
    } else {
        return 1;
    }

    return 0;
}
