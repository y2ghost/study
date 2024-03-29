#include "common.h"
#include <errno.h>
#include <limits.h>

#ifdef  PATH_MAX
static long pathmax = PATH_MAX;
#else
static long pathmax = 0;
#endif

#define PATH_MAX_GUESS  1024

static long xsi_version = 0;
static long posix_version = 0;

char *path_alloc(size_t *sizep)
{
    if (0 == posix_version) {
        posix_version = sysconf(_SC_VERSION);
    }

    if (0 == xsi_version) {
        xsi_version = sysconf(_SC_XOPEN_VERSION);
    }

    if (0 == pathmax) {
        errno = 0;
        pathmax = pathconf("/", _PC_PATH_MAX);
        
        if (pathmax < 0) {
            if (0 == errno) {
                pathmax = PATH_MAX_GUESS;
            } else {
                err_sys("pathconf error for _PC_PATH_MAX");
            }
        } else {
            pathmax++;
        }
    }

    size_t size = 0;
    if (posix_version<200112L && xsi_version<4) {
        size = pathmax + 1;
    } else {
        size = pathmax;
    }

    char *ptr = malloc(size);
    if (NULL == ptr) {
        err_sys("malloc error for pathname");
    }

    if (NULL != sizep) {
        *sizep = size;
    }

    return ptr;
}
