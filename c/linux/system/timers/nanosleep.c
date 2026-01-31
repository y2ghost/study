
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
    if (argc != 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s secs nanosecs\n", argv[0]);
    }

    struct timespec request;
    request.tv_sec = atol(argv[1]);
    request.tv_nsec = atol(argv[2]);

    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = sigintHandler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    struct timeval start;
    if (gettimeofday(&start, NULL) == -1) {
        err_quit("gettimeofday");
    }

    struct timeval finish;
    struct timespec remain;

    for (;;) {
        int s = nanosleep(&request, &remain);
        if (s == -1 && errno != EINTR) {
            err_quit("nanosleep");
        }

        if (gettimeofday(&finish, NULL) == -1) {
            err_quit("gettimeofday");
        }

        printf("Slept for: %9.6f secs\n", finish.tv_sec - start.tv_sec +
            (finish.tv_usec - start.tv_usec) / 1000000.0);
        if (s == 0) {
            break;
        }

        printf("Remaining: %2ld.%09ld\n", (long) remain.tv_sec, remain.tv_nsec);
        request = remain;               /* Next sleep is with remaining time */
    }

    printf("Sleep complete\n");
    exit(EXIT_SUCCESS);
}

