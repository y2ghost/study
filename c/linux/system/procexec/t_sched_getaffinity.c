#include "common.h"
#include <sched.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pid\n", argv[0]);
    }

    pid_t pid = atoi(argv[1]);
    cpu_set_t set;
    int s = sched_getaffinity(pid, sizeof(cpu_set_t), &set);

    if (s == -1) {
        err_sys("sched_getaffinity");
    }

    printf("CPUs:");
    for (int cpu = 0; cpu < CPU_SETSIZE; cpu++) {
        if (CPU_ISSET(cpu, &set)) {
            printf(" %d", cpu);
        }
    }

    printf("\n");
    exit(EXIT_SUCCESS);
}

