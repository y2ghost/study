#include "common.h"
#include <unistd.h>
#include <signal.h>
#include <string.h>

static char *str2 = NULL;
static int handled = 0;

// 演示不可重入的函数，导致结果异常的示例
static void handler(int sig)
{
    crypt(str2, "xx");
    handled++;
}

int main(int argc, char *argv[])
{
    if (argc != 3) {
        err_quit("%s str1 str2\n", argv[0]);
    }

    str2 = argv[2];
    char *cr1 = strdup(crypt(argv[1], "xx"));

    if (cr1 == NULL) {
        err_quit("strdup");
    }

    struct sigaction sa = {0};
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    for (int callNum = 1, mismatch = 0; ; callNum++) {
        if (strcmp(crypt(argv[1], "xx"), cr1) != 0) {
            mismatch++;
            printf("Mismatch on call %d (mismatch=%d handled=%d)\n",
                    callNum, mismatch, handled);
        }
    }
}

