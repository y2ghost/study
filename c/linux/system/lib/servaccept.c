#include "common.h"
#include <sys/socket.h>
#include <sys/un.h>
#include <time.h>
#include <errno.h>

#define STALE   30

int serv_accept(int listenfd, uid_t *uidptr)
{
    int err = 0;
    struct sockaddr_un un;

    char *name = malloc(sizeof(un.sun_path) + 1);
    if (NULL == name) {
        return -1;
    }

    socklen_t len = sizeof(un);
    int clifd = accept(listenfd, (struct sockaddr *)&un, &len);

    if (clifd < 0) {
        free(name);
        return -2;
    }

    len -= offsetof(struct sockaddr_un, sun_path);
    memcpy(name, un.sun_path, len);
    name[len] = 0;

    int rval = 0;
    struct stat statbuf;

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
        rval = -5;
        goto errout;
    }

    time_t staletime = time(NULL) - STALE;
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

