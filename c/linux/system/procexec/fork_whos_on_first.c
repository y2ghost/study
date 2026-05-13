#include "common.h"
#include <sys/wait.h>

// 演示父子进程的执行顺序不保证
int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [num-children]\n", argv[0]);
    }

    int numChildren = (argc > 1) ? atoi(argv[1]) : 1;
    setbuf(stdout, NULL);

    for (int j = 0; j < numChildren; j++) {
        pid_t childPid = fork();
        switch (childPid) {
        case -1:
            err_quit("fork");
        case 0:
            printf("%d child\n", j);
            _exit(EXIT_SUCCESS);
        default:
            printf("%d parent\n", j);
            wait(NULL);
            break;
        }
    }

    exit(EXIT_SUCCESS);
}

