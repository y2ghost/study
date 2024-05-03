#include "common.h"
#include <sys/socket.h>
#include <sys/un.h>
#include <errno.h>

#define QLEN 10

int serv_listen(const char *name)
{
    int err = 0;
    struct sockaddr_un un;

    if (strlen(name) >= sizeof(un.sun_path)) {
        errno = ENAMETOOLONG;
        return -1;
    }

    int fd = socket(AF_UNIX, SOCK_STREAM, 0);
    if (fd < 0) {
        return -2;
    }

    unlink(name);
    memset(&un, 0, sizeof(un));
    un.sun_family = AF_UNIX;
    strcpy(un.sun_path, name);
    int len = offsetof(struct sockaddr_un, sun_path) + strlen(name);
    int rval = bind(fd, (struct sockaddr *)&un, len);

    if (rval < 0) {
        rval = -3;
        goto errout;
    }

    rval = listen(fd, QLEN);
    if (rval < 0) {
        rval = -4;
        goto errout;
    }

    return fd;

errout:
    err = errno;
    close(fd);
    errno = err;
    return rval;
}
