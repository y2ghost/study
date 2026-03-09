#include "common.h"

// BSD语义的vfork函数示例，一般不建议使用了
// 现代的fork函数已经功能完备强大
int main(int argc, char *argv[])
{
    int istack = 222;
    switch (vfork()) {
    case -1:
        err_quit("vfork");
    case 0:
        sleep(3);
        write(STDOUT_FILENO, "Child executing\n", 16);
        istack *= 3;
        _exit(EXIT_SUCCESS);
    default:
        // 父进程阻塞，直到子进程退出
        write(STDOUT_FILENO, "Parent executing\n", 17);
        printf("istack=%d\n", istack);
        exit(EXIT_SUCCESS);
    }
}
