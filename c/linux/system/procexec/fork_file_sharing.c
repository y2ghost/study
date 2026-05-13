#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/wait.h>

// 共享文件的打开状态、偏移等
int main(int argc, char *argv[])
{
    char template[] = "/tmp/testXXXXXX";
    setbuf(stdout, NULL);
    int fd = mkstemp(template);

    if (fd == -1) {
        err_quit("mkstemp");
    }

    printf("File offset before fork(): %lld\n",
        (long long) lseek(fd, 0, SEEK_CUR));
    int flags = fcntl(fd, F_GETFL);

    if (flags == -1) {
        err_quit("fcntl - F_GETFL");
    }

    printf("O_APPEND flag before fork() is: %s\n",
        (flags & O_APPEND) ? "on" : "off");
    switch (fork()) {
    case -1:
        err_quit("fork");
    case 0:
        if (lseek(fd, 1000, SEEK_SET) == -1) {
            err_quit("lseek");
        }

        flags = fcntl(fd, F_GETFL);
        if (flags == -1) {
            err_quit("fcntl - F_GETFL");
        }

        flags |= O_APPEND;
        if (fcntl(fd, F_SETFL, flags) == -1) {
            err_quit("fcntl - F_SETFL");
        }

        _exit(EXIT_SUCCESS);
    default:
        if (wait(NULL) == -1) {
            err_quit("wait");
        }

        printf("Child has exited\n");
        printf("File offset in parent: %lld\n",
            (long long) lseek(fd, 0, SEEK_CUR));
        flags = fcntl(fd, F_GETFL);

        if (flags == -1) {
            err_quit("fcntl - F_GETFL");
        }

        printf("O_APPEND flag in parent is: %s\n",
            (flags & O_APPEND) ? "on" : "off");
        exit(EXIT_SUCCESS);
    }

    return 0;
}
