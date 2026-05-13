#include "print_wait_status.h"
#include "common.h"
#include <sys/wait.h>

/*
 * 操作示例
 * ulimit -c unlimited
 * ./child_status &
 * kill -ABRT 7591
 *
 */
int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [exit-status]\n", argv[0]);
    }

    switch (fork()) {
    case -1: err_quit("fork");
    case 0:
        printf("Child started with PID = %ld\n", (long) getpid());
        if (argc > 1) {
            exit(atoi(argv[1]));
        } else {
            for (;;) {
                pause();
            }
        }

        exit(EXIT_FAILURE);

    default:
        for (;;) {
            int status = 0;
            pid_t childPid = waitpid(-1, &status, WUNTRACED | WCONTINUED);

            if (childPid == -1) {
                err_quit("waitpid");
            }

            printf("waitpid() returned: PID=%ld; status=0x%04x (%d,%d)\n",
                (long) childPid,
                (unsigned int) status, status >> 8, status & 0xff);
            printWaitStatus(NULL, status);

            if (WIFEXITED(status) || WIFSIGNALED(status)) {
                exit(EXIT_SUCCESS);
            }
        }
    }
}
