#include "apue.h"

volatile sig_atomic_t quitflag;

static void sig_int(int signo)
{
    switch (signo) {
    case SIGINT:
        printf("\ninterrupt\n");
        break;
    case SIGQUIT:
        quitflag = 1;
        break;
    }
}

int main(void)
{
    sigset_t newmask;
    sigset_t oldmask;
    sigset_t zeromask;

    if (SIG_ERR == signal(SIGINT, sig_int)) {
        err_sys("signal(SIGINT) error");
    }

    if (SIG_ERR == signal(SIGQUIT, sig_int)) {
        err_sys("signal(SIGQUIT) error");
    }

    sigemptyset(&zeromask);
    sigemptyset(&newmask);
    sigaddset(&newmask, SIGQUIT);

    if (sigprocmask(SIG_BLOCK, &newmask, &oldmask) < 0) {
        err_sys("SIG_BLOCK error");
    }

    while (0 == quitflag) {
        sigsuspend(&zeromask);
    }

    quitflag = 0;
    if (sigprocmask(SIG_SETMASK, &oldmask, NULL) < 0) {
        err_sys("SIG_SETMASK error");
    }

    return 0;
}
