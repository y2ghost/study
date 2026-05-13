#include "errors.h"
#include <pthread.h>
#include <unistd.h>
#include <sched.h>

#define THREADS 5

typedef struct thread_tag {
    int index;
    pthread_t id;
} thread_t;

static thread_t threads[THREADS];
static int rr_min_priority;

static void *thread_routine(void *arg)
{
    thread_t *self = (thread_t*)arg;
    int my_policy = 0;
    struct sched_param my_param;
    int status = 0;
    char *policy_str = NULL;

    my_param.sched_priority = rr_min_priority + self->index;
    DPRINTF(("Thread %d will set SCHED_FIFO, priority %d\n",
        self->index, my_param.sched_priority));
    status = pthread_setschedparam(self->id, SCHED_RR, &my_param);

    if (0 != status) {
        err_abort(status, "Set sched");
    }

    status = pthread_getschedparam(self->id, &my_policy, &my_param);
    if (0 != status) {
        err_abort(status, "Get sched");
    }

    switch (my_policy) {
    case SCHED_FIFO:
        policy_str = "FIFO";
        break;
    case SCHED_RR:
        policy_str = "RR";
        break;
    case SCHED_OTHER:
        policy_str = "OTHER";
        break;
    default:
        policy_str = "UNKNOWN";
    }

    printf("thread_routine %d running at %s/%d\n",
        self->index, policy_str, my_param.sched_priority);
    return NULL;
}

int main(int argc, char *argv[])
{
    int count = 0;
    int status = 0;

    rr_min_priority = sched_get_priority_min(SCHED_RR);
    if (-1 == rr_min_priority) {
        errno_abort("Get SCHED_RR min priority");
    }

    for (count=0; count<THREADS; count++) {
        threads[count].index = count;
        status = pthread_create(&threads[count].id, NULL,
            thread_routine,(void*)&threads[count]);

        if (0 != status) {
            err_abort(status, "Create thread");
        }
    }

    for (count=0; count<THREADS; count++) {
        status = pthread_join(threads[count].id, NULL);
        if (0 != status) {
            err_abort(status, "Join thread");
        }
    }

    printf("Main exiting\n");
    return 0;
}
