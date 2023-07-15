#include "common.h"
#include <sched.h>

int main(int argc, char *argv[])
{
    struct sched_param sp;
    for (int j = 1; j < argc; j++) {
        int pol = sched_getscheduler(atol(argv[j]));
        if (pol == -1) {
            err_sys("sched_getscheduler");
        }

        if (sched_getparam(atol(argv[j]), &sp) == -1) {
            err_sys("sched_getparam");
        }

        printf("%s: %-5s ", argv[j],
            (pol == SCHED_OTHER) ? "OTHER" :
            (pol == SCHED_RR) ? "RR" :
            (pol == SCHED_FIFO) ? "FIFO" :
            (pol == SCHED_BATCH) ? "BATCH" :
            (pol == SCHED_IDLE) ? "IDLE" :
            "???");
        printf("%2d\n", sp.sched_priority);
    }

    exit(EXIT_SUCCESS);
}
