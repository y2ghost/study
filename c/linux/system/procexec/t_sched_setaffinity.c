#include "common.h"
#include <sched.h>

/*
 * 设置CPU青合度示例
 * 使用方法: ./t_sched_setaffinity pid mask
 */
int main(int argc, char *argv[])
{
    if (argc != 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pid mask\n", argv[0]);
    }

    pid_t pid = atoi(argv[1]);
    unsigned long mask = atol(argv[2]);

    cpu_set_t set;
    CPU_ZERO(&set);

    for (int cpu = 0; mask > 0; cpu++, mask >>= 1) {
        if (mask & 1) {
            CPU_SET(cpu, &set);
        }
    }

    if (sched_setaffinity(pid, sizeof(set), &set) == -1) {
        err_sys("sched_setaffinity");
    }

    exit(EXIT_SUCCESS);
}

