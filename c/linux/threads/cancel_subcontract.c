#include "errors.h"
#include <pthread.h>

#define THREADS 5

typedef struct team_tag {
   int join_i;
   pthread_t workers[THREADS];
} team_t;

static void *worker_routine(void *arg)
{
    int counter = 0;

    counter = 0;
    while (1) {
        if (0 == (counter % 1000)) {
            pthread_testcancel();
        }

        counter++;
    }

    return NULL;
}

static void cleanup(void *arg)
{
    int count = 0;
    int status = 0;
    team_t *team = (team_t *)arg;

    for (count=team->join_i; count<THREADS; count++) {
        status = pthread_cancel(team->workers[count]);
        if (0 != status) {
            err_abort(status, "Cancel worker");
        }

        status = pthread_detach(team->workers[count]);
        if (0 != status) {
            err_abort(status, "Detach worker");
        }

        printf("Cleanup: cancelled %d\n", count);
    }
}

static void *thread_routine(void *arg)
{
    team_t team;
    int count = 0;
    int status = 0;
    void *result = NULL;

    for (count=0; count<THREADS; count++) {
        status = pthread_create(&team.workers[count], NULL,
            worker_routine, NULL);
        if (0 != status) {
            err_abort(status, "Create worker");
        }
    }

    pthread_cleanup_push(cleanup,(void*)&team);
    for (team.join_i=0; team.join_i<THREADS; team.join_i++) {
        status = pthread_join(team.workers[team.join_i], &result);
        if (0 != status) {
            err_abort(status, "Join worker");
        }
    }

    pthread_cleanup_pop(0);
    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t thread_id;

    status = pthread_create(&thread_id, NULL, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create team");
    }

    sleep(5);
    printf("Cancelling...\n");

    status = pthread_cancel(thread_id);
    if (0 != status) {
        err_abort(status, "Cancel team");
    }

    status = pthread_join(thread_id, NULL);
    if (0 != status) {
        err_abort(status, "Join team");
    }

    return 0;
}
