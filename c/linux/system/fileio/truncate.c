#include "common.h"

// 截断文件示例
int main(int argc, char *argv[])
{
    if (argc != 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file length\n", argv[0]);
    }

    long length = atol(argv[2]);
    if (truncate(argv[1], length) == -1) {
        err_quit("truncate失败");
    }

    exit(EXIT_SUCCESS);
}

