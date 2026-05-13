#include "common.h"
#include <signal.h>
#include <sys/time.h>
#include <time.h>

static volatile sig_atomic_t gotAlarm = 0;

static void displayTimes(const char *msg, int includeTimer)
{
    struct itimerval itv;
    static struct timeval start;
    struct timeval curr;
    static int callNum = 0;

    if (callNum == 0) {
        if (gettimeofday(&start, NULL) == -1) {
            err_quit("gettimeofday");
        }
    }

    if (callNum % 20 == 0) {
        printf("       Elapsed   Value Interval\n");
    }

    if (gettimeofday(&curr, NULL) == -1) {
        err_quit("gettimeofday");
    }

    printf("%-7s %6.2f", msg, curr.tv_sec - start.tv_sec +
        (curr.tv_usec - start.tv_usec) / 1000000.0);
    if (includeTimer) {
        if (getitimer(ITIMER_REAL, &itv) == -1) {
            err_quit("getitimer");
        }

        printf("  %6.2f  %6.2f",
            itv.it_value.tv_sec + itv.it_value.tv_usec / 1000000.0,
            itv.it_interval.tv_sec + itv.it_interval.tv_usec / 1000000.0);
    }

    printf("\n");
    callNum++;
}

static void sigalrmHandler(int sig)
{
    gotAlarm = 1;
}

int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [secs [usecs [int-secs [int-usecs]]]]\n", argv[0]);
    }

    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = sigalrmHandler;

    if (sigaction(SIGALRM, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    struct itimerval itv;
    clock_t prevClock;
    itv.it_value.tv_sec = (argc > 1) ? atol(argv[1]) : 2;
    itv.it_value.tv_usec = (argc > 2) ? atol(argv[2]) : 0;
    itv.it_interval.tv_sec = (argc > 3) ? atol(argv[3]) : 0;
    itv.it_interval.tv_usec = (argc > 4) ? atol(argv[4]) : 0;
    int maxSigs = (itv.it_interval.tv_sec == 0 && itv.it_interval.tv_usec == 0) ? 1 : 3;
    displayTimes("START:", 0);

    if (setitimer(ITIMER_REAL, &itv, NULL) == -1) {
        err_quit("setitimer");
    }

    prevClock = clock();
    int sigCnt = 0;

    for (;;) {
        while (((clock() - prevClock) * 10 / CLOCKS_PER_SEC) < 5) {
            if (gotAlarm) {
                gotAlarm = 0;
                displayTimes("ALARM:", 1);
                sigCnt++;

                if (sigCnt >= maxSigs) {
                    printf("That's all folks\n");
                    exit(EXIT_SUCCESS);
                }
            }
        }

        prevClock = clock();
        displayTimes("Main: ", 1);
    }

    return 0;
}
