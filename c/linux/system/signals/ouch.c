#include "common.h"
#include <signal.h>

static void sigHandler(int sig)
{
    printf("Ouch!\n");
}

// 示例信号处理操作
int main(int argc, char *argv[])
{
    if (signal(SIGINT, sigHandler) == SIG_ERR) {
        err_quit("signal");
    }

    for (int j = 0; ; j++) {
        printf("%d\n", j);
        sleep(3);
    }
}

