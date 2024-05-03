#include "common.h"
#include <fcntl.h>

pid_t lock_test(int fd, int type, off_t offset, int whence, off_t len)
{
    struct flock lock;
    memset(&lock, 0x0, sizeof(lock));
    /* F_RDLCK 或者 F_WRLCK */
    lock.l_type = type;
    lock.l_start = offset;
    /* SEEK_SET, SEEK_CUR, SEEK_END */
    lock.l_whence = whence;
    lock.l_len = len;

    if (fcntl(fd, F_GETLK, &lock) < 0) {
        err_sys("fcntl error");
    }

    if (F_UNLCK == lock.l_type) {
        return 0;
    }
    
    return lock.l_pid;
}

