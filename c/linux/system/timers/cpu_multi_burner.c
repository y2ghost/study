#include <sys/times.h>
#include <time.h>
#include "common.h"

#define NANO 1000000000

static void burnCPU(float period)
{
    struct timespec prev_rt = {0};
    if (clock_gettime(CLOCK_REALTIME, &prev_rt) == -1) {
        err_quit("clock_gettime");
    }


    int prev_step = 0;
    while (1) {
        struct timespec curr_cpu = {0};
        if (clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &curr_cpu) == -1) {
            err_quit("clock_gettime");
        }

        int curr_step = curr_cpu.tv_sec / period + curr_cpu.tv_nsec / period / NANO;
        struct timespec curr_rt = {0};

        if (clock_gettime(CLOCK_REALTIME, &curr_rt) == -1) {
            err_quit("clock_gettime");
        }

        int elapsed_rt_us = (curr_rt.tv_sec - prev_rt.tv_sec) * 1000000 +
            (curr_rt.tv_nsec - prev_rt.tv_nsec) / 1000;
        if (curr_step > prev_step) {
            printf("[%ld]  %%CPU = %.2f; totCPU = %.3f\n",
                (long) getpid(),
                period / (elapsed_rt_us / 1000000.0) * 100.0,
                (float) curr_step * period);
            prev_step = curr_step;
            prev_rt = curr_rt;
        }
    }
}

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s [period]...\n"
            "Creates one process per argument that reports "
            "CPU time each 'period' CPU seconds\n"
            "'period' can be a floating-point number\n", argv[0]);
    }

    int nproc = argc - 1;
    for (int j = 0; j < nproc; j++) {
        float period;
        switch (fork()) {
        case 0:
            sscanf(argv[j + 1], "%f", &period);
            burnCPU(period);
            exit(EXIT_SUCCESS);
        case -1:
            err_quit("fork");
        default:
            break;
        }
    }

    pause();
    exit(EXIT_SUCCESS);
}

