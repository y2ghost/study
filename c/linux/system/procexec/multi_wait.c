#include "curr_time.h"
#include "common.h"
#include <sys/wait.h>
#include <time.h>
#include <errno.h>

// 等待子进程退出示例
// 用法: ./multi_wait  7 1 4
int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s sleep-time...\n", argv[0]);
    }

    setbuf(stdout, NULL);
    for (int j = 1; j < argc; j++) {
        switch (fork()) {
        case -1:
            err_quit("fork");
        case 0:
            printf("[%s] child %d started with PID %ld, sleeping %s "
                "seconds\n", currTime("%T"), j, (long) getpid(),
                argv[j]);
            sleep(atoi(argv[j]));
            _exit(EXIT_SUCCESS);
        default:
            break;
        }
    }

    int numDead = 0;
    for (;;) {
        pid_t childPid = wait(NULL);
        if (childPid == -1) {
            if (errno == ECHILD) {
                printf("No more children - bye!\n");
                exit(EXIT_SUCCESS);
            } else {
                err_quit("wait");
            }
        }

        numDead++;
        printf("[%s] wait() returned child PID %ld (numDead=%d)\n",
            currTime("%T"), (long) childPid, numDead);
    }
}
