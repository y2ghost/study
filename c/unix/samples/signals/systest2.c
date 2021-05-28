#include "apue.h"

static void sig_int(int signo)
{
    printf("caught SIGINT\n");
}

static void sig_chld(int signo)
{
    printf("caught SIGCHLD\n");
}

int main(void)
{
    if (SIG_ERR == signal(SIGINT, sig_int)) {
        err_sys("signal(SIGINT) error");
    }

    if (SIG_ERR == signal(SIGCHLD, sig_chld)) {
        err_sys("signal(SIGCHLD) error");
    }

    if (system("/bin/ed") < 0) {
        err_sys("system() error");
    }

    return 0;
}
