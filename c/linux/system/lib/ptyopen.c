#include "common.h"
#include <errno.h>
#include <fcntl.h>

int ptym_open(char *pts_name, int pts_namesz)
{
    int err = 0;
    int fdm = 0;

    if ((fdm = posix_openpt(O_RDWR)) < 0) {
        return -1;
    }

    if (grantpt(fdm) < 0) {
        goto errout;
    }

    if (unlockpt(fdm) < 0) {
        goto errout;
    }

    char *ptr = ptsname(fdm);
    if (NULL == ptr) {
        goto errout;
    }

    strncpy(pts_name, ptr, pts_namesz);
    pts_name[pts_namesz-1] = '\0';
    return(fdm);

errout:
    err = errno;
    close(fdm);
    errno = err;
    return -1;
}

int ptys_open(char *pts_name)
{
    int fds = open(pts_name, O_RDWR);
    return fds;
}
