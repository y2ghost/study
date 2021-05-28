#include "apue.h"
#include <sys/socket.h>
#include <sys/un.h>
#include <errno.h>

#define CLI_PATH    "/var/tmp/"
#define CLI_PERM    S_IRWXU

static void do_err_(int fd, int err_no, const char *path)
{
    int err = 0;

    err = err_no;
    close(fd);
    unlink(path);
    errno = err;
}

/*
 * Create a client endpoint and connect to a server.
 * Returns fd if all OK, <0 on error.
 */
int cli_conn(const char *name)
{
    int fd = 0;
    int len = 0;
    int rval = 0;
    struct sockaddr_un un;
    struct sockaddr_un sun;

    if (strlen(name) >= sizeof(un.sun_path)) {
        errno = ENAMETOOLONG;
        return -1;
    }

    /* create a UNIX domain stream socket */
    fd = socket(AF_UNIX, SOCK_STREAM, 0);
    if (fd < 0) {
        return -1;
    }

    /* fill socket address structure with our address */
    memset(&un, 0x0, sizeof(un));
    un.sun_family = AF_UNIX;
    snprintf(un.sun_path, sizeof(un.sun_path), "%s%d", CLI_PATH, getpid());
    printf("file is %s\n", un.sun_path);
    len = offsetof(struct sockaddr_un, sun_path) + strlen(un.sun_path);
    unlink(un.sun_path);

    rval = bind(fd, (struct sockaddr *)&un, len);
    if (rval < 0) {
        do_err_(fd, errno, un.sun_path);
        return -2;
    }

    rval = chmod(un.sun_path, CLI_PERM);
    if (rval < 0) {
        do_err_(fd, errno, un.sun_path);
        return -3;
    }

    memset(&sun, 0x0, sizeof(sun));
    sun.sun_family = AF_UNIX;
    snprintf(sun.sun_path, sizeof(sun.sun_path), "%s", name);
    len = offsetof(struct sockaddr_un, sun_path) + strlen(sun.sun_path);

    rval = connect(fd, (struct sockaddr *)&sun, len);
    if (rval < 0) {
        do_err_(fd, errno, un.sun_path);
        return -4;
    }

    return fd;
}
