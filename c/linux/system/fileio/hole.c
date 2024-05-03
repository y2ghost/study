#include "common.h"
#include <fcntl.h>

char buf1[] = "abcdefghij";
char buf2[] = "ABCDEFGHIJ";

int main(void)
{
    int fd = 0;

    fd = creat("file.hole", FILE_MODE);
    if (fd < 0) {
        err_sys("creat error");
    }

    if (10 != write(fd, buf1, 10)) {
        err_sys("buf1 write error");
    }

    /* offset now = 10 */
    if (-1 == lseek(fd, 16384, SEEK_SET)) {
        err_sys("lseek error");
    }

    /* offset now = 16384 */
    if (10 != write(fd, buf2, 10)) {
        err_sys("buf2 write error");
    }

    /* offset now = 16394 */
    return 0;
}
