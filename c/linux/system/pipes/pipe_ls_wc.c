#include "common.h"
#include <sys/wait.h>

int main(int argc, char *argv[])
{
    int pfd[2] = {0};
    if (pipe(pfd) == -1) {
        err_sys("pipe");
    }

    switch (fork()) {
    case -1:
        err_sys("fork");
    case 0:
        if (close(pfd[0]) == -1) {
            err_sys("close 1");
        }

        if (pfd[1] != STDOUT_FILENO) {
            if (dup2(pfd[1], STDOUT_FILENO) == -1) {
                err_sys("dup2 1");
            }

            if (close(pfd[1]) == -1) {
                err_sys("close 2");
            }
        }

        execlp("ls", "ls", (char *) NULL);
        err_sys("execlp ls");
    default:
        break;
    }

    switch (fork()) {
    case -1:
        err_sys("fork");
    case 0:
        if (close(pfd[1]) == -1) {
            err_sys("close 3");
        }

        if (pfd[0] != STDIN_FILENO) {
            if (dup2(pfd[0], STDIN_FILENO) == -1) {
                err_sys("dup2 2");
            }

            if (close(pfd[0]) == -1) {
                err_sys("close 4");
            }
        }

        execlp("wc", "wc", "-l", (char *) NULL);
        err_sys("execlp wc");
    default:
        break;
    }

    if (close(pfd[0]) == -1) {
        err_sys("close 5");
    }

    if (close(pfd[1]) == -1) {
        err_sys("close 6");
    }

    if (wait(NULL) == -1) {
        err_sys("wait 1");
    }

    if (wait(NULL) == -1) {
        err_sys("wait 2");
    }

    exit(EXIT_SUCCESS);
}
