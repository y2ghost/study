#include "common.h"
#include <errno.h>
#include <sys/socket.h>

int initserver(int type, const struct sockaddr *addr,
socklen_t alen, int qlen)
{
    int fd = 0;
    int err = 0;
    int reuse = 1;

    fd = socket(addr->sa_family, type, 0);

    if (fd < 0) {
        return -1;
    }

    if (setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(int)) < 0) {
        goto errout;
    }

    if (bind(fd, addr, alen) < 0) {
        goto errout;
    }

    if (SOCK_STREAM==type || SOCK_SEQPACKET==type) {
        if (listen(fd, qlen) < 0) {
            goto errout;
        }
    }

    return fd;

errout:
    err = errno;
    close(fd);
    errno = err;
    return -1;
}
