#include "common.h"
#include <fcntl.h>

/* 关闭某个flag功能 */
void clr_fl(int fd, int flags)
{
    int val = fcntl(fd, F_GETFL, 0);
    if (val < 0) {
        err_sys("fcntl F_GETFL error");
    }

    /* 关闭flags功能 */
    val &= ~flags;
    val = fcntl(fd, F_SETFL, val);
    if (val < 0) {
        err_sys("fcntl F_SETFL error");
    }
}
