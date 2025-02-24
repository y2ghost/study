#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>

#define CMD_SIZE 200
#define BUF_SIZE 1024

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s temp-file [num-1kB-blocks] \n", argv[0]);
    }

    int numBlocks = (argc > 2) ? atoi(argv[2]) : 100000;
    int fd = open(argv[1], O_WRONLY | O_CREAT | O_EXCL, S_IRUSR | S_IWUSR);

    if (fd == -1) {
        err_quit("open");
    }

    if (unlink(argv[1]) == -1) {
        err_quit("unlink");
    }

    char buf[BUF_SIZE] = {0};
    for (int j = 0; j < numBlocks; j++) {
        if (write(fd, buf, BUF_SIZE) != BUF_SIZE) {
            err_quit("partial/failed write");
        }
    }

    char shellCmd[CMD_SIZE] = {0};
    snprintf(shellCmd, CMD_SIZE, "df -k `dirname %s`", argv[1]);
    system(shellCmd);

    if (close(fd) == -1) {
        err_quit("close");
    }

    printf("********** Closed file descriptor\n");
    system(shellCmd);
    exit(EXIT_SUCCESS);
}

