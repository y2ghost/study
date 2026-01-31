#include "common.h"
#include <signal.h>
#include <errno.h>

int main(int argc, char *argv[])
{
    if (argc != 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pid sig-num\n", argv[0]);
    }

    int sig = atoi(argv[2]);
    int s = kill(atol(argv[1]), sig);

    if (sig != 0) {
        if (s == -1) {
            err_quit("kill");
        }
    } else {
        if (s == 0) {
            printf("Process exists and we can send it a signal\n");
        } else {
            if (errno == EPERM) {
                printf("Process exists, but we don't have "
                       "permission to send it a signal\n");
            } else if (errno == ESRCH) {
                printf("Process does not exist\n");
            } else {
                err_quit("kill");
            }
        }
    }

    exit(EXIT_SUCCESS);
}

