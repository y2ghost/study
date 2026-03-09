#include "common.h"
#include <ctype.h>

#define BUF_SIZE 100

int main(int argc, char *argv[])
{
    int outbound[2] = {0};
    if (pipe(outbound) == -1) {
        err_sys("pipe");
    }

    int inbound[2] = {0};
    if (pipe(inbound) == -1) {
        err_sys("pipe");
    }

    char buf[BUF_SIZE] = {0};
    ssize_t cnt = 0;

    switch (fork()) {
    case -1:
        err_sys("fork");
    case 0:
        if (close(outbound[1]) == -1) {
            err_sys("close");
        }

        if (close(inbound[0]) == -1) {
            err_sys("close");
        }

        while ((cnt = read(outbound[0], buf, BUF_SIZE)) > 0) {
            for (int j = 0; j < cnt; j++) {
                buf[j] = toupper((unsigned char) buf[j]);
            }

            if (write(inbound[1], buf, cnt) != cnt) {
                err_quit("failed/partial write(): inbound pipe");
            }
        }

        if (cnt == -1) {
            err_sys("read");
        }

        exit(EXIT_SUCCESS);
    default:
        if (close(outbound[0]) == -1) {
            err_sys("close");
        }

        if (close(inbound[1]) == -1) {
            err_sys("close");
        }

        while ((cnt = read(STDIN_FILENO, buf, BUF_SIZE)) > 0) {
            if (write(outbound[1], buf, cnt) != cnt) {
                err_quit("failed/partial write(): outbound pipe");
            }

            cnt = read(inbound[0], buf, BUF_SIZE);
            if (cnt == -1) {
                err_sys("read");
            }

            if (cnt > 0) {
                if (write(STDOUT_FILENO, buf, cnt) != cnt) {
                    err_quit("failed/partial write(): STDOUT_FILENO");
                }
            }
        }

        if (cnt == -1) {
            err_sys("read");
        }

        exit(EXIT_SUCCESS);
    }
}
