#include "signal_functions.h"
#include "common.h"
#include <string.h>
#include <signal.h>
#include <time.h>
#include <errno.h>

static volatile int sigintCnt = 0;
static volatile sig_atomic_t gotSigquit = 0;

static void handler(int sig)
{
    printf("Caught signal %d (%s)\n", sig, strsignal(sig));
    if (sig == SIGQUIT) {
        gotSigquit = 1;
    }

    sigintCnt++;
}

int main(int argc, char *argv[])
{
    sigset_t origMask = {0};
    sigset_t blockMask = {0};
    sigemptyset(&blockMask);
    sigaddset(&blockMask, SIGINT);
    sigaddset(&blockMask, SIGQUIT);
    printSigMask(stdout, "Initial signal mask is:\n");

    if (sigprocmask(SIG_BLOCK, &blockMask, &origMask) == -1) {
        err_quit("sigprocmask - SIG_BLOCK");
    }

    struct sigaction sa = {0};
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    if (sigaction(SIGQUIT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    for (int loopNum = 1; !gotSigquit; loopNum++) {
        printf("=== LOOP %d\n", loopNum);
        printSigMask(stdout, "Starting critical section, signal mask is:\n");

        for (time_t startTime = time(NULL); time(NULL) < startTime + 4; ) {
            continue;
        }

        printPendingSigs(stdout, "Before sigsuspend() - pending signals:\n");
        if (sigsuspend(&origMask) == -1 && errno != EINTR) {
            err_quit("sigsuspend");
        }
    }

    if (sigprocmask(SIG_SETMASK, &origMask, NULL) == -1) {
        err_quit("sigprocmask - SIG_SETMASK");
    }

    printSigMask(stdout, "=== Exited loop\nRestored signal mask to:\n");
    exit(EXIT_SUCCESS);
}

