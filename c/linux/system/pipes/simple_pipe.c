#include "common.h"
#include <sys/wait.h>

#define BUF_SIZE 10

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s string\n", argv[0]);
    }

    // pfd[0]读取，pfd[1]写入
    int pfd[2] = {0};
    if (pipe(pfd) == -1) {
        err_sys("pipe");
    }

    switch (fork()) {
    case -1:
        err_sys("fork");
    case 0:
        if (close(pfd[1]) == -1) {
            err_sys("close - child");
        }

        char buf[BUF_SIZE] = {0};
        for (;;) {
            ssize_t numRead = read(pfd[0], buf, BUF_SIZE);
            if (numRead == -1) {
                err_sys("read");
            }

            if (numRead == 0) {
                break;
            }

            if (write(STDOUT_FILENO, buf, numRead) != numRead) {
                err_quit("child - partial/failed write");
            }
        }

        write(STDOUT_FILENO, "\n", 1);
        if (close(pfd[0]) == -1) {
            err_sys("close");
        }

        exit(EXIT_SUCCESS);
    default:
        if (close(pfd[0]) == -1) {
            err_sys("close - parent");
        }

        if (write(pfd[1], argv[1], strlen(argv[1])) != strlen(argv[1])) {
            err_quit("parent - partial/failed write");
        }

        if (close(pfd[1]) == -1) {
            err_sys("close");
        }

        wait(NULL);
        exit(EXIT_SUCCESS);
    }
}
