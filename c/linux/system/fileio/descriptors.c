#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>

#define FILE "test.file"

// 文件描述共享示例
int main(int argc, char *argv[])
{
    char cmd[] = "cat " FILE "; echo";
    int fd1 = open(FILE, O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);

    if (fd1 == -1) {
        err_quit("open fd1");
    }

    // dup函数返回最小可用的文件描述符
    int fd2 = dup(fd1);
    if (fd2 == -1) {
        err_quit("dup");
    }

    int fd3 = open(FILE, O_RDWR);
    if (fd3 == -1) {
        err_quit("open fd3");
    }

    /*
     * fd1 和 fd2共享相同的打开文件表
     * fd3拥有自己的打开文件表
     */
    if (write(fd1, "Hello fd1,", 10) == -1) {
        err_quit("write1");
    }

    system(cmd);
    if (write(fd2, "fd2 world", 9) == -1) {
        err_quit("write2");
    }

    system(cmd);
    if (lseek(fd2, 0, SEEK_SET) == -1) {
        err_quit("lseek");
    }

    if (write(fd1, "HELLO ", 6) == -1) {
        err_quit("write3");
    }

    system(cmd);
    if (write(fd3, "FD3   ", 6) == -1) {
        err_quit("write4");
    }

    system(cmd);
    if (close(fd1) == -1) {
        err_quit("close output fd1");
    }

    if (close(fd2) == -1) {
        err_quit("close output fd2");
    }

    if (close(fd3) == -1) {
        err_quit("close output fd3");
    }

    exit(EXIT_SUCCESS);
}

