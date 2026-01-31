#include "errors.h"
#include <pthread.h>

static int counter = 0;

static void *thread_routine(void *arg)
{
    int state = 0;
    int status= 0;

    counter = 0;
    while (1) {
        if (0 == (counter % 755)) {
            status = pthread_setcancelstate(PTHREAD_CANCEL_DISABLE, &state);
            if (0 != status) {
                err_abort(status, "Disable cancel");
            }

            sleep(1);
            status = pthread_setcancelstate(state, &state);

            if (0 != status) {
                err_abort(status, "Restore cancel");
            }
        } else {
            if (0 == (counter % 1000)) {
                pthread_testcancel();
            }
        }

        counter++;
    }
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
    status = pthread_cancel(thread_id);

    if (0 != status) {
        err_abort(status, "Cancel thread");
    }

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
