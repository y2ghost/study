#include <sys/times.h>
#include <time.h>
#include <stdio.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

static long CLOCK_TICKS = 0;

static void error(const char *err)
{
    fprintf(stderr, "%s!\n", err);
    exit(1);
}

static void _display_time(const char *msg)
{
    if (NULL != msg) {
        printf("%s", msg);
    }

    clock_t clock_time = clock();
    if (-1 == clock_time) {
        error("clock");
    }

    printf("  clock return: %ld clocks_per_sec (%.2f secs)\n",
        clock_time, (double)clock_time / CLOCKS_PER_SEC);
    struct tms t = {0};

    if (-1 == times(&t)) {
        error("times");
    }

    printf("   times yields: user=%.2f, system=%.2f\n",
        (double)t.tms_utime / CLOCK_TICKS,
        (double)t.tms_stime / CLOCK_TICKS);
}

int main(int ac, char *av[])
{
    CLOCK_TICKS = sysconf(_SC_CLK_TCK);
    if (-1 == CLOCK_TICKS) {
        error("sysconf");
    }

    printf("clocks_per_sec=%ld, sysconf=%ld\n\n",
        CLOCKS_PER_SEC, CLOCK_TICKS);
    _display_time("At program start:\n");

    int i = 0;
    for (i=0; i<100000000; ++i) {
        getppid();
    }

    _display_time("After getppid loop:\n");
    return 0;
}
