#include "errors.h"
#include <unistd.h>
#include <pthread.h>
#include <sched.h>

static const char *get_policy_name(int policy)
{
    const char *policy_name = NULL;

    switch (policy) {
    case SCHED_FIFO:
        policy_name = "FIFO";
        break;
    case SCHED_RR:
        policy_name = "RR";
        break;
    case SCHED_OTHER:
        policy_name = "OTHER";
        break;
    default:
        policy_name = "UNKOWN";
        break;
    }

    return policy_name;
}

static void *thread_routine(void *arg)
{
    int status = 0;
    int my_policy = 0;
    struct sched_param my_param;
    const char *my_policy_name = NULL;

#if defined(_POSIX_THREAD_PRIORITY_SCHEDULING)
    status = pthread_getschedparam(pthread_self(), &my_policy, &my_param);
    if (0 != status) {
        err_abort(status, "Get sched");
    }

    my_policy_name = get_policy_name(my_policy);
    printf("thread_routine running at %s/%d\n",
        my_policy_name,
        my_param.sched_priority);
#endif 
    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    int thread_policy = 0;
    int rr_min_priority = 0;
    int rr_max_priority = 0;
    pthread_t thread_id;
    pthread_attr_t thread_attr;
    struct sched_param thread_param;
    const char *thread_policy_name = NULL;

    status = pthread_attr_init(&thread_attr);
    if (0 != status) {
        err_abort(status, "Init attr");
    }

#if defined(_POSIX_THREAD_PRIORITY_SCHEDULING)
    status = pthread_attr_getschedpolicy(&thread_attr, &thread_policy);
    if (0 != status) {
        err_abort(status, "Get policy");
    }

    status = pthread_attr_getschedparam(&thread_attr, &thread_param);
    if (0 != status) {
        err_abort(status, "Get sched param");
    }

    thread_policy_name = get_policy_name(thread_policy);
    printf("Default policy is %s, priority is %d\n",
        thread_policy_name,
        thread_param.sched_priority);
    status = pthread_attr_setschedpolicy(&thread_attr, SCHED_RR);

    if (0 != status) {
        printf("Unable to set SCHED_RR policy.\n");
    } else {
        rr_min_priority = sched_get_priority_min(SCHED_RR);
        if (rr_min_priority == -1) {
            errno_abort("Get SCHED_RR min priority");
        }

        rr_max_priority = sched_get_priority_max(SCHED_RR);
        if (rr_max_priority == -1) {
            errno_abort("Get SCHED_RR max priority");
        }

        thread_param.sched_priority = (rr_min_priority+rr_max_priority) / 2;
        printf("SCHED_RR priority range is %d to %d: using %d\n",
            rr_min_priority,
            rr_max_priority,
            thread_param.sched_priority);
        status = pthread_attr_setschedparam(&thread_attr, &thread_param);

        if (0 != status) {
            err_abort(status, "Set params");
        }

        printf("Creating thread at RR/%d\n", thread_param.sched_priority);
        status = pthread_attr_setinheritsched(&thread_attr, PTHREAD_EXPLICIT_SCHED);

        if (0 != status) {
            err_abort(status, "Set inherit");
        }
    }
#endif 

    status = pthread_create(&thread_id, &thread_attr, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    status = pthread_join(thread_id, NULL);
    if (0 != status) {
        err_abort(status, "Join thread");
    }

    printf("Main exiting\n");
    return 0;
}
