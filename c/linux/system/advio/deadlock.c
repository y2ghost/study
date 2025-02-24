#include "common.h"
#include <fcntl.h>

static void lockabyte(const char *name, int fd, off_t offset)
{
    if (writew_lock(fd, offset, SEEK_SET, 1) < 0) {
        err_sys("%s: writew_lock error", name);
    }

    printf("%s: got the lock, byte %lld\n", name, (long long)offset);
}

int main(void)
{
    int fd = 0;
    pid_t pid = 0;

    fd = creat("templock", FILE_MODE);
    if (fd < 0) {
        err_sys("creat error");
    }

    if (2 != write(fd,"ab",2)) {
        err_sys("write error");
    }

    TELL_WAIT();
    pid = fork();

    switch (pid) {
    case -1:
        err_sys("fork error");
        break;
    case 0:
        lockabyte("child", fd, 0);
        TELL_PARENT(getppid());
        WAIT_PARENT();
        lockabyte("child", fd, 1);
        exit(0);
        break;
  default:
        lockabyte("parent", fd, 1);
        TELL_CHILD(pid);
        WAIT_CHILD();
        lockabyte("parent", fd, 0);
        exit(0);
    }

    return 0;
}
