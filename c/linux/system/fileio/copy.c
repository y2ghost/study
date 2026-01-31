#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>

#ifndef BUF_SIZE
#define BUF_SIZE 1024
#endif

int main(int argc, char *argv[])
{
    if (argc != 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s old-file new-file\n", argv[0]);
    }

    int inputFd = open(argv[1], O_RDONLY);
    if (inputFd == -1) {
        err_quit("opening file %s", argv[1]);
    }

    int openFlags = O_CREAT | O_WRONLY | O_TRUNC;
    mode_t filePerms = S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH;

    int outputFd = open(argv[2], openFlags, filePerms);
    if (outputFd == -1) {
        err_quit("opening file %s", argv[2]);
    }

    ssize_t numRead = 0;
    char buf[BUF_SIZE] = {0};

    while ((numRead = read(inputFd, buf, BUF_SIZE)) > 0) {
        if (write(outputFd, buf, numRead) != numRead) {
            err_quit("write() returned error or partial write occurred");
        }
    }

    if (numRead == -1) {
        err_quit("read");
    }

    if (close(inputFd) == -1) {
        err_quit("close input");
    }

    if (close(outputFd) == -1) {
        err_quit("close output");
    }

    exit(EXIT_SUCCESS);
}
