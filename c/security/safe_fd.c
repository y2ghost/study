#include <sys/types.h>
#include <limits.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <paths.h>

#ifndef OPEN_MAX
#define OPEN_MAX 256
#endif

static int _open_devnull(int fd) {
    FILE *f = NULL;
    if (0 == fd) {
        f = freopen(_PATH_DEVNULL, "rb", stdin);
    } else if (1 == fd) {
        f = freopen(_PATH_DEVNULL, "wb", stdout);
    } else if (2 == fd) {
        f = freopen(_PATH_DEVNULL, "wb", stderr);
    }
    
    return (f && fileno(f) == fd);
}

void _safe_fd(void) {
    int fd = 0;
    int fds = 0;
    struct stat st;
    
    /* Make sure all open descriptors other than
     * the standard ones are closed */
    fds = getdtablesize();
    if (-1 == fds) {
        fds = OPEN_MAX;
    }

    for (fd=3; fd<fds; fd++) {
        close(fd);
    }

    /* Verify that the standard descriptors are open.
     * If they're not, attempt to open them using /dev/null.
     * If any are unsuccessful, abort. */
    for (fd=0; fd<3; fd++) {
        if (fstat(fd, &st) == -1
            && (errno != EBADF || !_open_devnull(fd))) {
            abort();
        }
    }
}

int main(int ac, char *av[])
{
    _safe_fd();
    return 0;
}
