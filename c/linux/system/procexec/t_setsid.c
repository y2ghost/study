#include "common.h"
#include <unistd.h>
#include <fcntl.h>

/*
 * 设置会话ID示例
 * 查看会话ID命令: ps -p $$ -o 'pid pgid sid command'
 * 执行程序: ./t_setsid
 */

int main(int argc, char *argv[])
{
    if (fork() != 0) {
        _exit(EXIT_SUCCESS);
    }

    if (setsid() == -1) {
        err_sys("setsid");
    }

    printf("PID=%ld, PGID=%ld, SID=%ld\n", (long) getpid(),
        (long) getpgrp(), (long) getsid(0));

    // 会失败，没关系，因为没有控制终端
    if (open("/dev/tty", O_RDWR) == -1) {
        err_sys("open /dev/tty");
    }

    exit(EXIT_SUCCESS);
}
