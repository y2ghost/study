#include "common.h"
#include <fcntl.h>

int set_cloexec(int fd)
{
    int val = fcntl(fd, F_GETFD, 0);
    if (val < 0) {
        return -1;
    }

    val |= FD_CLOEXEC;
    return fcntl(fd, F_SETFD, val);
}

