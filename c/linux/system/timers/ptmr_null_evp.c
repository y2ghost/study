#include "curr_time.h"
#include "common.h"
#include <signal.h>
#include <time.h>

static void
handler(int sig, siginfo_t *si, void *uc)
{
    printf("[%s] Got signal %d\n", currTime("%T"), sig);
    printf("    sival_int          = %d\n", si->si_value.sival_int);
    printf("    si_overrun         = %d\n", si->si_overrun);
    printf("    timer_getoverrun() = %d\n",
        timer_getoverrun((timer_t) si->si_value.sival_ptr));
}

int main(int argc, char *argv[])
{
    if (argc < 2) {
        err_quit("%s secs [nsecs [int-secs [int-nsecs]]]\n", argv[0]);
    }

    struct sigaction sa = {0};
    sa.sa_flags = SA_SIGINFO;
    sa.sa_sigaction = handler;
    sigemptyset(&sa.sa_mask);

    if (sigaction(SIGALRM, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    timer_t tid = {0};
    if (timer_create(CLOCK_REALTIME, NULL, &tid) == -1) {
        err_quit("timer_create");
    }

    printf("timer ID = %ld\n", (long) tid);
    struct itimerspec ts = {0};
    ts.it_value.tv_sec = atoi(argv[1]);
    ts.it_value.tv_nsec = (argc > 2) ? atoi(argv[2]) : 0;
    ts.it_interval.tv_sec = (argc > 3) ? atoi(argv[3]) : 0;
    ts.it_interval.tv_nsec = (argc > 4) ? atoi(argv[4]) : 0;

    if (timer_settime(tid, 0, &ts, NULL) == -1) {
        err_quit("timer_settime");
    }

    for (int j = 0; ; j++) {
        pause();
    }
}
