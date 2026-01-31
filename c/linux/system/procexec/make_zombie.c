
#include "common.h"
#include <signal.h>
#include <libgen.h>

#define CMD_SIZE 200

int main(int argc, char *argv[])
{
    char cmd[CMD_SIZE] = {0};
    setbuf(stdout, NULL);
    printf("Parent PID=%ld\n", (long) getpid());

    pid_t childPid = fork();
    switch (childPid) {
    case -1:
        err_quit("fork");
    case 0:
        // 子进程立即退出，父进程不执行wait便产生僵尸进程
        printf("Child (PID=%ld) exiting\n", (long) getpid());
        _exit(EXIT_SUCCESS);
    default:
        sleep(3);
        snprintf(cmd, CMD_SIZE, "ps | grep %s", basename(argv[0]));
        system(cmd);

        if (kill(childPid, SIGKILL) == -1) {
            err_msg("kill");
        }

        sleep(3);
        printf("After sending SIGKILL to zombie (PID=%ld):\n", (long) childPid);
        system(cmd);
        exit(EXIT_SUCCESS);
    }
}
