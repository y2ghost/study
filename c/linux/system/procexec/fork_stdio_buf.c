#include "common.h"

/*
 * 示例缓存复制导致打印两次相同信息
 * 终端执行程序时，默认行缓冲，所以立即打印
 * printf输出内容，然后打印write函数写入的内容
 * 如果重定向到文件，因为是块缓冲，所以进程退出
 * 时才会刷新到文件中去，就会多次打印printf函数输出内容
 */
int main(int argc, char *argv[])
{
    printf("Hello world\n");
    write(STDOUT_FILENO, "Ciao\n", 5);

    if (fork() == -1) {
        err_quit("fork");
    }

    exit(EXIT_SUCCESS);
}

