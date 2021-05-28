#include "apue.h"
#include <fcntl.h>

/* flags are the file status flags to turn off */
void clr_fl(int fd, int flags)
{
    int val = 0;

    val = fcntl(fd, F_GETFL, 0);
    if (val < 0) {
        err_sys("fcntl F_GETFL error");
    }

    /* turn flags off */
    val &= ~flags;
    val = fcntl(fd, F_SETFL, val);
    if (val < 0) {
        err_sys("fcntl F_SETFL error");
    }
}
