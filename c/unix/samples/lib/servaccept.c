#include "apue.h"
#include <sys/socket.h>
#include <sys/un.h>
#include <time.h>
#include <errno.h>

#define STALE   30

/*
 * Wait for a client connection to arrive, and accept it.
 * We also obtain the client's user ID from the pathname
 * that it must bind before calling us.
 * Returns new fd if all OK, <0 on error
 */
int serv_accept(int listenfd, uid_t *uidptr)
{
    int clifd =0;
    int err = 0;
    int rval = 0;
    socklen_t len = 0;
    time_t staletime = 0;
    struct sockaddr_un un;
    struct stat statbuf;
    char *name = NULL;

    name = malloc(sizeof(un.sun_path) + 1);
    if (NULL == name) {
        return -1;
    }

    len = sizeof(un);
    clifd = accept(listenfd, (struct sockaddr *)&un, &len);

    if (clifd < 0) {
        free(name);
        return -2;
    }

    /* obtain the client's uid from its calling address */
    len -= offsetof(struct sockaddr_un, sun_path);
    memcpy(name, un.sun_path, len);
    name[len] = 0;

    if (stat(name, &statbuf) < 0) {
        rval = -3;
        goto errout;
    }

    if (0 == S_ISSOCK(statbuf.st_mode)) {
        rval = -4;
        goto errout;
    }

    if ((statbuf.st_mode & (S_IRWXG|S_IRWXO)) ||
        (statbuf.st_mode & S_IRWXU)!=S_IRWXU) {
        /* is not rwx------ */
        rval = -5;
        goto errout;
    }

    staletime = time(NULL) - STALE;
    if (statbuf.st_atime<staletime ||
        statbuf.st_ctime<staletime ||
        statbuf.st_mtime<staletime) {
        rval = -6;
        goto errout;
    }

    if (NULL != uidptr) {
        *uidptr = statbuf.st_uid;
    }

    unlink(name);
    free(name);
    return clifd;

errout:
    err = errno;
    close(clifd);
    free(name);
    errno = err;
    return rval;
}
