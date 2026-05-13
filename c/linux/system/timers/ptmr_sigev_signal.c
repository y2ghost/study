#include "curr_time.h"
#include "itimerspec_from_str.h"
#include "common.h"
#include <signal.h>
#include <time.h>

#define TIMER_SIG SIGRTMAX

static void handler(int sig, siginfo_t *si, void *uc)
{
    timer_t *tidptr = si->si_value.sival_ptr;
    printf("[%s] Got signal %d\n", currTime("%T"), sig);
    printf("    *sival_ptr         = %ld\n", (long) *tidptr);
    printf("    timer_getoverrun() = %d\n", timer_getoverrun(*tidptr));
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

    struct sigaction sa = {0};
    sa.sa_flags = SA_SIGINFO;
    sa.sa_sigaction = handler;
    sigemptyset(&sa.sa_mask);

    if (sigaction(TIMER_SIG, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    struct itimerspec ts = {0};
    struct sigevent sev = {0};
    sev.sigev_notify = SIGEV_SIGNAL;
    sev.sigev_signo = TIMER_SIG;

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

    for (;;) {
        pause();
    }

    return 0;
}

