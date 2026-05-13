#include "common.h"
#include <sys/times.h>
#include <time.h>
#include <signal.h>

static volatile sig_atomic_t gotSig = 0;

static void handler(int sig)
{
    gotSig = 1;
}

int main(int argc, char *argv[])
{
    struct sigaction sa = {0};
    sa.sa_handler = handler;
    sa.sa_flags = 0;
    sigemptyset(&sa.sa_mask);

    if (sigaction(SIGTERM, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    struct timespec prev_rt;
    if (clock_gettime(CLOCK_REALTIME, &prev_rt) == -1) {
        err_quit("clock_gettime");
    }

    time_t prev_cpu_secs = 0;
    struct timespec curr_cpu = {0};

    while (!gotSig) {
        if (clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &curr_cpu) == -1) {
            err_quit("clock_gettime");
        }

        if (curr_cpu.tv_sec > prev_cpu_secs) {
            struct timespec curr_rt = {0};
            if (clock_gettime(CLOCK_REALTIME, &curr_rt) == -1) {
                err_quit("clock_gettime");
            }

            int elapsed_us = (curr_rt.tv_sec - prev_rt.tv_sec) * 1000000 +
                (curr_rt.tv_nsec - prev_rt.tv_nsec) / 1000;
            printf("[%ld]  %%CPU = %5.2f; totCPU = %ld.%03ld\n",
                (long) getpid(), 1000000.0 / elapsed_us * 100.0,
                (long) curr_cpu.tv_sec, curr_cpu.tv_nsec / 1000000);
            prev_cpu_secs = curr_cpu.tv_sec;
            prev_rt = curr_rt;
        }
    }

    printf("Bye!\n");
    exit(EXIT_SUCCESS);
}

