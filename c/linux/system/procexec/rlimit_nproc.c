#include "print_rlimit.h"
#include "common.h"
#include <sys/resource.h>

// 执行示例: ./rlimit_nproc 30 100
int main(int argc, char *argv[])
{
    if (argc < 2 || argc > 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s soft-limit [hard-limit]\n", argv[0]);
    }

    printRlimit("Initial maximum process limits: ", RLIMIT_NPROC);
    struct rlimit rl;
    rl.rlim_cur = (argv[1][0] == 'i') ? RLIM_INFINITY : atoi(argv[1]);
    rl.rlim_max = (argc == 2) ? rl.rlim_cur : 
        (argv[2][0] == 'i') ? RLIM_INFINITY : atoi(argv[2]);

    if (setrlimit(RLIMIT_NPROC, &rl) == -1) {
        err_sys("setrlimit");
    }

    printRlimit("New maximum process limits:     ", RLIMIT_NPROC);
    for (int j = 1; ; j++) {
        pid_t childPid = fork();
        switch (childPid)  {
        case -1: err_sys("fork");
        case 0: _exit(EXIT_SUCCESS);
        default:
            printf("Child %d (PID=%ld) started\n", j, (long) childPid);
            break;
        }
    }
}
