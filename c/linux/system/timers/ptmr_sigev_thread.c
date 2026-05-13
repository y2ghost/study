#include "curr_time.h"
#include "common.h"
#include "itimerspec_from_str.h"
#include <signal.h>
#include <time.h>
#include <pthread.h>

static pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t cond = PTHREAD_COND_INITIALIZER;

static int expireCnt = 0;

static void threadFunc(union sigval sv)
{
    timer_t *tidptr = sv.sival_ptr;
    printf("[%s] Thread notify\n", currTime("%T"));
    printf("    timer ID=%ld\n", (long) *tidptr);
    printf("    timer_getoverrun()=%d\n", timer_getoverrun(*tidptr));
    int s = pthread_mutex_lock(&mtx);

    if (s != 0) {
        err_quit("pthread_mutex_lock");
    }

    expireCnt += 1 + timer_getoverrun(*tidptr);
    s = pthread_mutex_unlock(&mtx);

    if (s != 0) {
        err_quit("pthread_mutex_unlock");
    }

    s = pthread_cond_signal(&cond);
    if (s != 0) {
        err_quit("pthread_cond_signal");
    }
}

int main(int argc, char *argv[])
{
    if (argc < 2) {
        err_quit("%s secs[/nsecs][:int-secs[/int-nsecs]]...\n", argv[0]);
    }

    timer_t *tidlist = calloc(argc - 1, sizeof(timer_t));
    if (tidlist == NULL) {
        err_quit("malloc");
    }

    struct sigevent sev = {0};
    struct itimerspec ts = {0};
    sev.sigev_notify = SIGEV_THREAD;
    sev.sigev_notify_function = threadFunc;
    sev.sigev_notify_attributes = NULL;

    for (int j = 0; j < argc - 1; j++) {
        itimerspecFromStr(argv[j + 1], &ts);
        sev.sigev_value.sival_ptr = &tidlist[j];

        if (timer_create(CLOCK_REALTIME, &sev, &tidlist[j]) == -1) {
            err_quit("timer_create");
        }

        printf("Timer ID: %ld (%s)\n", (long) tidlist[j], argv[j + 1]);
        if (timer_settime(tidlist[j], 0, &ts, NULL) == -1) {
            err_quit("timer_settime");
        }
    }


    int s = pthread_mutex_lock(&mtx);
    if (s != 0) {
        err_quit("pthread_mutex_lock");
    }

    for (;;) { 
        s = pthread_cond_wait(&cond, &mtx);
        if (s != 0) {
            err_quit("pthread_cond_wait");
        }

        printf("main(): expireCnt = %d\n", expireCnt);
    }

    return 0;
}
