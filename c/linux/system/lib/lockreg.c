#include "common.h"
#include <fcntl.h>

int lock_reg(int fd, int cmd, int type, off_t offset, int whence, off_t len)
{
    struct flock lock;
    memset(&lock, 0x0, sizeof(lock));
    /* F_RDLCK, F_WRLCK, F_UNLCK */
    lock.l_type = type;
    /* 相对l_whence的字节偏移 */
    lock.l_start = offset;
    /* SEEK_SET, SEEK_CUR, SEEK_END */
    lock.l_whence = whence;
    lock.l_len = len;
    return fcntl(fd, cmd, &lock);
}
