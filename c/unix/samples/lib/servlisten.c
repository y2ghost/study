#include "apue.h"
#include <sys/socket.h>
#include <sys/un.h>
#include <errno.h>

#define QLEN 10

/*
 * Create a server endpoint of a connection.
 * Returns fd if all OK, <0 on error.
 */
int serv_listen(const char *name)
{
    int fd = 0;
    int len = 0;
    int err = 0;
    int rval = 0;
    struct sockaddr_un un;

    if (strlen(name) >= sizeof(un.sun_path)) {
        errno = ENAMETOOLONG;
        return -1;
    }

    /* create a UNIX domain stream socket */
    fd = socket(AF_UNIX, SOCK_STREAM, 0);
    if (fd < 0) {
        return -2;
    }

    unlink(name);
    memset(&un, 0, sizeof(un));
    un.sun_family = AF_UNIX;
    strcpy(un.sun_path, name);
    len = offsetof(struct sockaddr_un, sun_path) + strlen(name);

    /* bind the name to the descriptor */
    rval = bind(fd, (struct sockaddr *)&un, len);
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
