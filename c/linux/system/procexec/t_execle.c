#include "common.h"

// 执行示例: ./t_execle /bin/echo
int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pathname\n", argv[0]);
    }

    char *filename = strrchr(argv[1], '/');
    if (filename != NULL) {
        filename++;
    } else {
        filename = argv[1];
    }

    char *envVec[] = { "GREET=salut", "BYE=adieu", NULL };
    execle(argv[1], filename, "hello world", "goodbye", (char *) NULL, envVec);
    err_quit("execle");
}

