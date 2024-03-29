#include "common.h"
#include <sys/syscall.h>
#include <sched.h>
#include <sys/times.h>
#include <time.h>
#include <signal.h>
#include <pthread.h>
#include <sys/types.h>

#define NANO 1000000000

static pid_t tid(void)
{
    return syscall(SYS_gettid);
}

static void *threadFunc(void *arg)
{
    float period = 0;
    char *sarg = arg;
    sscanf(sarg, "%f", &period);
    struct timespec prev_rt = {0};

    if (clock_gettime(CLOCK_REALTIME, &prev_rt) == -1) {
        err_quit("clock_gettime");
    }

    long long nloops = 0;
    int prev_step = 0;

    while (1) {
        int j = 0;
        int k = 0;

        for (j = 0, k = 0; j < 1000; j++) {
            k = j;
        }

        // 避免被代码优化掉
        if (k >= 0) {
            k++;
        }

        nloops++;
        struct timespec curr_cpu = {0};

        if (clock_gettime(CLOCK_THREAD_CPUTIME_ID, &curr_cpu) == -1) {
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
            printf("[%ld]  %%CPU = %.2f; totCPU = %.3f\n", (long) tid(),
                period / (elapsed_rt_us / 1000000.0) * 100.0,
                (float) curr_step * period);
            prev_step = curr_step;
            prev_rt = curr_rt;
            nloops = 0;
        }
    }

    return NULL;
}

int main(int argc, char *argv[])
{

    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s [period]...\n"
            "Creates one thread per argument that reports "
            "CPU time each 'period' CPU seconds\n"
            "'period' can be a floating-point number\n", argv[0]);
    }

    pthread_t thr;
    for (int j = 1; j < argc; j++) {
        int s = pthread_create(&thr, NULL, threadFunc, argv[j]);
        if (s != 0) {
            err_quit("pthread_create");
        }
    }

    pause();
    exit(EXIT_SUCCESS);
}
