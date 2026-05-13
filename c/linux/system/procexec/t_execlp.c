#include "common.h"

// 执行示例: ./t_execlp /bin/echo
int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pathname\n", argv[0]);
    }

    execlp(argv[1], argv[1], "hello world", (char *) NULL);
    err_quit("execlp");
}
