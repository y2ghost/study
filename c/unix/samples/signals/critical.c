#include "apue.h"

static void sig_quit(int);

int main(void)
{
    sigset_t newmask;
    sigset_t oldmask;
    sigset_t pendmask;

    if (SIG_ERR == signal(SIGQUIT, sig_quit)) {
        err_sys("can't catch SIGQUIT");
    }

    sigemptyset(&newmask);
    sigaddset(&newmask, SIGQUIT);

    if (sigprocmask(SIG_BLOCK, &newmask, &oldmask) < 0) {
        err_sys("SIG_BLOCK error");
    }

    sleep(5);
    if (sigpending(&pendmask) < 0) {
        err_sys("sigpending error");
    }

    if (sigismember(&pendmask, SIGQUIT)) {
        printf("\nSIGQUIT pending\n");
    }

    if (sigprocmask(SIG_SETMASK, &oldmask, NULL) < 0) {
        err_sys("SIG_SETMASK error");
    }

    printf("SIGQUIT unblocked\n");
    sleep(5);
    exit(0);
}

static void sig_quit(int signo)
{
    printf("caught SIGQUIT\n");
    if (SIG_ERR == signal(SIGQUIT, SIG_DFL)) {
        err_sys("can't reset SIGQUIT");
    }
}
