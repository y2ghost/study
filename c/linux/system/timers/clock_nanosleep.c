#include "common.h"
#include <sys/time.h>
#include <time.h>
#include <signal.h>
#include <errno.h>

static void sigintHandler(int sig)
{
    return;
}

int main(int argc, char *argv[])
{
    if (argc < 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s secs nanosecs [a]\n", argv[0]);
    }

    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = sigintHandler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    int flags = (argc > 3) ? TIMER_ABSTIME : 0;
    struct timespec request;
    long argSecs = atol(argv[1]);
    long argNanoSecs = atol(argv[2]);

    if (flags == TIMER_ABSTIME) {
        if (clock_gettime(CLOCK_REALTIME, &request) == -1) {
            err_quit("clock_gettime");
        }

        printf("Initial CLOCK_REALTIME value: %ld.%09ld\n",
            (long) request.tv_sec, request.tv_nsec);
        request.tv_sec  += argSecs;
        request.tv_nsec += argNanoSecs;

        if (request.tv_nsec >= 1000000000) {
            request.tv_sec += request.tv_nsec / 1000000000;
            request.tv_nsec %= 1000000000;
        }

    } else {
        request.tv_sec  = argSecs;
        request.tv_nsec = argNanoSecs;
    }

    struct timeval start, finish;
    if (gettimeofday(&start, NULL) == -1) {
        err_quit("gettimeofday");
    }

    for (;;) {
        struct timespec remain;
        int s = clock_nanosleep(CLOCK_REALTIME, flags, &request, &remain);

        if (s != 0 && s != EINTR) {
            err_quit("clock_nanosleep");
        }

        if (s == EINTR) {
            printf("Interrupted... ");
        }

        if (gettimeofday(&finish, NULL) == -1) {
            err_quit("gettimeofday");
        }

        printf("Slept: %.6f secs", finish.tv_sec - start.tv_sec +
            (finish.tv_usec - start.tv_usec) / 1000000.0);

        if (s == 0) {
            break;
        }

        if (flags != TIMER_ABSTIME) {
            printf("... Remaining: %ld.%09ld", (long) remain.tv_sec, remain.tv_nsec);
            request = remain;
        }

        printf("... Restarting\n");
    }

    printf("\nSleep complete\n");
    exit(EXIT_SUCCESS);
}
