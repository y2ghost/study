#include "common.h"

extern char **environ;

// 打印程序的环境变量示例
int main(int argc, char *argv[])
{
    for (char **ep = environ; *ep != NULL; ep++) {
        puts(*ep);
    }

    exit(EXIT_SUCCESS);
}

