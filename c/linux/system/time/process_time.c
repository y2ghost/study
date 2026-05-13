#include "common.h"
#include <sys/times.h>
#include <time.h>

// 显示进程时间
static void displayProcessTimes(const char *msg)
{
    static long clockTicks = 0;
    if (msg != NULL) {
        printf("%s", msg);
    }

    if (clockTicks == 0) {
        clockTicks = sysconf(_SC_CLK_TCK);
        if (clockTicks == -1) {
            err_quit("sysconf");
        }
    }

    clock_t clockTime = clock();
    if (clockTime == -1) {
        err_quit("clock");
    }

    printf("        clock() returns: %ld clocks-per-sec (%.2f secs)\n",
            (long) clockTime, (double) clockTime / CLOCKS_PER_SEC);
    struct tms t;

    if (times(&t) == -1) {
        err_quit("times");
    }

    printf("        times() yields: user CPU=%.2f; system CPU: %.2f\n",
        (double) t.tms_utime / clockTicks,
        (double) t.tms_stime / clockTicks);
}

int main(int argc, char *argv[])
{
    printf("CLOCKS_PER_SEC=%ld  sysconf(_SC_CLK_TCK)=%ld\n\n",
            (long) CLOCKS_PER_SEC, sysconf(_SC_CLK_TCK));
    displayProcessTimes("At program start:\n");
    int numCalls = (argc > 1) ? atoi(argv[1]) : 100000000;

    for (int j = 0; j < numCalls; j++) {
        (void) getppid();
    }

    displayProcessTimes("After getppid() loop:\n");
    exit(EXIT_SUCCESS);
}

