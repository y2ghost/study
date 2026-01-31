#include "common.h"
#include <unistd.h>

// 打开或是关闭进程记账功能
int main(int argc, char *argv[])
{
    if (argc > 2 || (argc > 1 && strcmp(argv[1], "--help") == 0)) {
        err_quit("%s [file]\n", argv[0]);
    }

    if (acct(argv[1]) == -1) {
        err_quit("error acct");
    }

    printf("Process accounting %s\n",
        (argv[1] == NULL) ? "disabled" : "enabled");
    exit(EXIT_SUCCESS);
}
