#include "common.h"
#include <string.h>
#include <signal.h>
#include <stdbool.h>

static void sigfpeCatcher(int sig)
{
    printf("Caught signal %d (%s)\n", sig, strsignal(sig));
    sleep(1);
}

int main(int argc, char *argv[])
{
    if (argc > 1 && strchr(argv[1], 'i') != NULL) {
        printf("Ignoring SIGFPE\n");
        if (signal(SIGFPE, SIG_IGN) == SIG_ERR) {
            err_quit("signal");
        }
    } else {
        printf("Catching SIGFPE\n");
        struct sigaction sa;
        sigemptyset(&sa.sa_mask);
        sa.sa_flags = SA_RESTART;
        sa.sa_handler = sigfpeCatcher;

        if (sigaction(SIGFPE, &sa, NULL) == -1) {
            err_quit("sigaction");
        }
    }

    bool blocking = argc > 1 && strchr(argv[1], 'b') != NULL;
    sigset_t prevMask;

    if (blocking) {
        printf("Blocking SIGFPE\n");
        sigset_t blockSet;
        sigemptyset(&blockSet);
        sigaddset(&blockSet, SIGFPE);

        if (sigprocmask(SIG_BLOCK, &blockSet, &prevMask) == -1) {
            err_quit("sigprocmask");
        }
    }

    printf("About to generate SIGFPE\n");
    int x, y;
    y = 0;
    x = 1 / y;
    y = x;

    if (blocking) {
        printf("Sleeping before unblocking\n");
        sleep(2);
        printf("Unblocking SIGFPE\n");

        if (sigprocmask(SIG_SETMASK, &prevMask, NULL) == -1) {
            err_quit("sigprocmask");
        }
    }

    printf("Shouldn't get here!\n");
    exit(EXIT_FAILURE);
}

