#include "common.h"
#include <errno.h>
#include <fcntl.h>
#include <sys/wait.h>

int main(int argc, char *argv[])
{
    int fd = 0;
    pid_t pid = 0;
    char buf[5] = {'\0'};
    struct stat statbuf;

    if (2 != argc) {
        fprintf(stderr, "usage: %s filename\n", argv[0]);
        exit(1);
    }

    fd = open(argv[1], O_RDWR | O_CREAT | O_TRUNC, FILE_MODE);
    if (fd < 0) {
        err_sys("open error");
    }

    if (6 != write(fd, "abcdef", 6)) {
        err_sys("write error");
    }

    /* turn on set-group-ID and turn off group-execute */
    if (fstat(fd, &statbuf) < 0) {
        err_sys("fstat error");
    }

    if (fchmod(fd, (statbuf.st_mode & ~S_IXGRP) | S_ISGID) < 0) {
        err_sys("fchmod error");
    }

    TELL_WAIT();

    pid = fork();
    switch (pid) {
    case -1:
        err_sys("fork error");
        break;
    case 0:
        /* goining outside */
        break;
    default:
        /* write lock entire file */
        if (write_lock(fd, 0, SEEK_SET, 0) < 0) {
            err_sys("write_lock error");
        }

        TELL_CHILD(pid);
        if (waitpid(pid, NULL, 0) < 0) {
            err_sys("waitpid error");
        }
        
        exit(0);
        break;
    }

    WAIT_PARENT();
    set_fl(fd, O_NONBLOCK);

    /* first let's see what error we get if region is locked */
    if (-1 != read_lock(fd, 0, SEEK_SET, 0)) {
        err_sys("child: read_lock succeeded");
    }

    printf("read_lock of already-locked region returns %d\n", errno);
    /* now try to read the mandatory locked file */
    if (-1 == lseek(fd, 0, SEEK_SET)) {
        err_sys("lseek error");
    }

    if (read(fd, buf, 2) < 0) {
        err_ret("read failed (mandatory locking works)");
    } else {
        printf("read OK (no mandatory locking), buf = %2.2s\n", buf);
    }

    return 0;
}
