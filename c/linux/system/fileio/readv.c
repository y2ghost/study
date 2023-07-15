#include "common.h"
#include <sys/stat.h>
#include <sys/uio.h>
#include <fcntl.h>

#define STR_SIZE 100

//执行分散输入的示例
int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file\n", argv[0]);
    }

    int fd = open(argv[1], O_RDONLY);
    if (fd == -1) {
        err_quit("open出错");
    }

    ssize_t totRequired = 0;
    struct iovec iov[3] = {0};
    // 分散输入到如下三个变量中
    struct stat myStruct = {0};
    iov[0].iov_base = &myStruct;
    iov[0].iov_len = sizeof(struct stat);
    totRequired += iov[0].iov_len;

    int x = 0;
    iov[1].iov_base = &x;
    iov[1].iov_len = sizeof(x);
    totRequired += iov[1].iov_len;

    char str[STR_SIZE] = {0};
    iov[2].iov_base = str;
    iov[2].iov_len = STR_SIZE;
    totRequired += iov[2].iov_len;

    ssize_t numRead = readv(fd, iov, 3);
    if (numRead == -1) {
        err_quit("readv出错");
    }

    if (numRead < totRequired) {
        printf("文件内容不够分散输入填充\n");
    }

    printf("分散输入请求大小: %ld; 实际读取大小: %ld\n",
            (long) totRequired, (long) numRead);
    exit(EXIT_SUCCESS);
}

