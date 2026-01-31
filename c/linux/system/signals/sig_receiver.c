#include "signal_functions.h"
#include "common.h"
#include <signal.h>

static int sigCnt[NSIG] = {0};
static volatile sig_atomic_t gotSigint = 0;

static void handler(int sig)
{
    if (sig == SIGINT) {
        gotSigint = 1;
    } else {
        sigCnt[sig]++;
    }
}

int main(int argc, char *argv[])
{
    printf("%s: PID is %ld\n", argv[0], (long) getpid());
    for (int n = 1; n < NSIG; n++) {
        (void) signal(n, handler);
    }

    if (argc > 1) {
        sigset_t pendingMask = {0}, blockingMask = {0}, emptyMask = {0};
        int numSecs = atoi(argv[1]);
        sigfillset(&blockingMask);

        if (sigprocmask(SIG_SETMASK, &blockingMask, NULL) == -1) {
            err_quit("sigprocmask");
        }

        printf("%s: sleeping for %d seconds\n", argv[0], numSecs);
        sleep(numSecs);

        if (sigpending(&pendingMask) == -1) {
            err_quit("sigpending");
        }

        printf("%s: pending signals are: \n", argv[0]);
        printSigset(stdout, "\t\t", &pendingMask);

        sigemptyset(&emptyMask);
        if (sigprocmask(SIG_SETMASK, &emptyMask, NULL) == -1) {
            err_quit("sigprocmask");
        }
    }

    while (!gotSigint) {
        continue;
    }

    for (int i = 1; i < NSIG; i++) {
        if (sigCnt[i] != 0) {
            printf("%s: signal %d caught %d time%s\n", argv[0], i,
                    sigCnt[i], (sigCnt[i] == 1) ? "" : "s");
        }
    }

    exit(EXIT_SUCCESS);
}

