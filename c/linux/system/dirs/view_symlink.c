#include "common.h"
#include <sys/stat.h>
#include <limits.h>

#define BUF_SIZE PATH_MAX

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pathname\n", argv[0]);
    }

    struct stat statbuf = {0};
    if (lstat(argv[1], &statbuf) == -1) {
        err_quit("lstat");
    }

    if (!S_ISLNK(statbuf.st_mode)) {
        err_quit("%s is not a symbolic link", argv[1]);
    }

    char buf[BUF_SIZE] = {0};
    ssize_t numBytes = readlink(argv[1], buf, BUF_SIZE - 1);

    if (numBytes == -1) {
        err_quit("readlink");
    }

    buf[numBytes] = '\0';
    printf("readlink: %s --> %s\n", argv[1], buf);

    if (realpath(argv[1], buf) == NULL) {
        err_quit("realpath");
    }

    printf("realpath: %s --> %s\n", argv[1], buf);
    exit(EXIT_SUCCESS);
}

