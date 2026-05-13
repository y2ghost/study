#include "errors.h"
#include <pthread.h>

#define SPIN    10000000

static long counter = 0;
static time_t end_time = 0;
static pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

static void *counter_thread(void *arg)
{
    int status = 0;
    int spin = 0;

    while (time(NULL) < end_time) {
        status = pthread_mutex_lock(&mutex);
        if (0 != status) {
            err_abort(status, "Lock mutex");
        }

        for (spin=0; spin<SPIN; spin++) {
            counter++;
        }

        status = pthread_mutex_unlock(&mutex);
        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }

        sleep(1);
    }

    printf("Counter is %ld\n", counter);
    return NULL;
}

static void *monitor_thread(void *arg)
{
    int status = 0;
    int misses = 0;

    while (time(NULL) < end_time) {
        sleep(3);
        status = pthread_mutex_trylock(&mutex);

        if (status != EBUSY) {
            if (0 != status) {
                err_abort(status, "Trylock mutex");
            }

            printf("Counter is %ld\n", counter/SPIN);
            status = pthread_mutex_unlock(&mutex);

            if (0 != status) {
                err_abort(status, "Unlock mutex");
            }
        } else {
            misses++;
        }
    }

    printf("Monitor thread missed update %d times.\n", misses);
    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t counter_thread_id;
    pthread_t monitor_thread_id;

    end_time = time(NULL) + 60;
    status = pthread_create(&counter_thread_id, NULL, counter_thread, NULL);

    if (0 != status) {
        err_abort(status, "Create counter thread");
    }

    status = pthread_create(&monitor_thread_id, NULL, monitor_thread, NULL);
    if (0 != status) {
        err_abort(status, "Create monitor thread");
    }

    status = pthread_join(counter_thread_id, NULL);
    if (0 != status) {
        err_abort(status, "Join counter thread");
    }

    status = pthread_join(monitor_thread_id, NULL);
    if (0 != status) {
        err_abort(status, "Join monitor thread");
    }

    return 0;
}
