#include "common.h"
#include "itimerspec_from_str.h"
#include <sys/timerfd.h>
#include <time.h>
#include <stdint.h>

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s secs[/nsecs][:int-secs[/int-nsecs]] [max-exp]\n", argv[0]);
    }

    struct itimerspec ts = {0};
    itimerspecFromStr(argv[1], &ts);
    int maxExp = (argc > 2) ? atoi(argv[2]) : 1;
    int fd = timerfd_create(CLOCK_REALTIME, 0);

    if (fd == -1) {
        err_quit("timerfd_create");
    }

    if (timerfd_settime(fd, 0, &ts, NULL) == -1) {
        err_quit("timerfd_settime");
    }

    struct timespec start = {0};
    if (clock_gettime(CLOCK_MONOTONIC, &start) == -1) {
        err_quit("clock_gettime");
    }

    struct timespec now = {0};
    uint64_t numExp = 0;

    for (uint64_t totalExp = 0; totalExp < maxExp;) {
        ssize_t s = read(fd, &numExp, sizeof(uint64_t));
        if (s != sizeof(uint64_t)) {
            err_quit("read");
        }

        totalExp += numExp;
        if (clock_gettime(CLOCK_MONOTONIC, &now) == -1) {
            err_quit("clock_gettime");
        }

        int secs = now.tv_sec - start.tv_sec;
        int nanosecs = now.tv_nsec - start.tv_nsec;

        if (nanosecs < 0) { 
            secs--;
            nanosecs += 1000000000;
        }

        printf("%d.%03d: expirations read: %llu; total=%llu\n",
            secs, (nanosecs + 500000) / 1000000,
            (unsigned long long) numExp, (unsigned long long) totalExp);
    }

    exit(EXIT_SUCCESS);
    return 0;
}
