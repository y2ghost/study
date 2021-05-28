#include "apue.h"

static void sig_usr(int);

int main(void)
{
    if (SIG_ERR == signal(SIGUSR1, sig_usr)) {
        err_sys("can't catch SIGUSR1");
    }

    if (SIG_ERR == signal(SIGUSR2, sig_usr)) {
        err_sys("can't catch SIGUSR2");
    }

    while (1) {
        pause();
    }

    return 0;
}

static void sig_usr(int signo)
{
    switch (signo) {
    case SIGUSR1:
        printf("received SIGUSR1\n");
        break;
    case SIGUSR2:
        printf("received SIGUSR2\n");
        break;
    default:
        err_dump("received signal %d\n", signo);
    }
}
