#include "errors.h"
#include <pthread.h>

static int counter = 0;

static void *thread_routine(void *arg)
{
    counter = 0;
    DPRINTF(("thread_routine starting\n"));

    while (1) {
        if (0 == (counter%1000)) {
            DPRINTF(("calling testcancel\n"));
            pthread_testcancel();
        }

        counter++;
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    void *result = NULL;
    pthread_t thread_id;

    status = pthread_create(&thread_id, NULL, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    sleep(2);
    DPRINTF(("calling cancel\n"));
    status = pthread_cancel(thread_id);

    if (0 != status) {
        err_abort(status, "Cancel thread");
    }

    DPRINTF(("calling join\n"));
    status = pthread_join(thread_id, &result);

    if (0 != status) {
        err_abort(status, "Join thread");
    }

    if (PTHREAD_CANCELED == result) {
        printf("Thread cancelled at iteration %d\n", counter);
    } else {
        printf("Thread was not cancelled\n");
    }

    return 0;
}
