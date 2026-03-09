#include <string.h>
#include <signal.h>
#include "signal_functions.h"
#include "common.h"

static void handler(int sig)
{
    printf("Caught signal %d (%s)\n", sig, strsignal(sig));
    fflush(NULL);
}

int main(int argc, char *argv[])
{
    const int numSecs = 5;
    printf("Setting up handler for SIGINT\n");
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    sigset_t blocked;
    sigemptyset(&blocked);
    sigaddset(&blocked, SIGINT);

    if (sigprocmask(SIG_SETMASK, &blocked, NULL) == -1) {
        err_quit("sigprocmask");
    }

    printf("BLOCKING SIGINT for %d seconds\n", numSecs);
    sleep(numSecs);
    sigset_t pending;

    if (sigpending(&pending) == -1) {
        err_quit("sigpending");
    }

    printf("PENDING signals are: \n");
    printSigset(stdout, "\t\t", &pending);
    sleep(2);
    printf("Ignoring SIGINT\n");

    if (signal(SIGINT, SIG_IGN) == SIG_ERR) {
        err_quit("signal");
    }

    if (sigpending(&pending) == -1) {
        err_quit("sigpending");
    }

    if (sigismember(&pending, SIGINT)) {
        printf("SIGINT is now pending\n");
    } else {
        printf("PENDING signals are: \n");
        printSigset(stdout, "\t\t", &pending);
    }

    sleep(2);
    printf("Reestablishing handler for SIGINT\n");
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    sleep(2);
    printf("UNBLOCKING SIGINT\n");
    sigemptyset(&blocked);

    if (sigprocmask(SIG_SETMASK, &blocked, NULL) == -1) {
        err_quit("sigprocmask");
    }

    exit(EXIT_SUCCESS);
}

