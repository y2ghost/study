#include "apue.h"
#include <fcntl.h>

pid_t lock_test(int fd, int type, off_t offset, int whence, off_t len)
{
    struct flock lock;
    
    memset(&lock, 0x0, sizeof(lock));
    /* F_RDLCK or F_WRLCK */
    lock.l_type = type;
    /* byte offset, relative to l_whence */
    lock.l_start = offset;
    /* SEEK_SET, SEEK_CUR, SEEK_END */
    lock.l_whence = whence;
    lock.l_len = len;

    if (fcntl(fd, F_GETLK, &lock) < 0) {
        err_sys("fcntl error");
    }

    if (F_UNLCK == lock.l_type) {
        /* false, region isn't locked by another proc */
        return 0;
    }
    
    /* true, return pid of lock owner */
    return lock.l_pid;
}
