#include "common.h"
#include <signal.h>
#include <errno.h>

#define TESTSIG SIGUSR1

static void handler(int sig)
{
}

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s num-sigs\n", argv[0]);
    }

    int numSigs = atoi(argv[1]);
    struct sigaction sa = {0};
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(TESTSIG, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    sigset_t blockedMask = {0}, emptyMask = {0};
    sigemptyset(&blockedMask);
    sigaddset(&blockedMask, TESTSIG);

    if (sigprocmask(SIG_SETMASK, &blockedMask, NULL) == -1) {
        err_quit("sigprocmask");
    }

    sigemptyset(&emptyMask);
    pid_t childPid = fork();

    switch (childPid) {
        case -1: err_quit("fork");
        case 0:
            for (int scnt = 0; scnt < numSigs; scnt++) {
                if (kill(getppid(), TESTSIG) == -1) {
                    err_quit("kill");
                }
            }

            if (sigsuspend(&emptyMask) == -1 && errno != EINTR) {
                    err_quit("sigsuspend");
            }
        
            exit(EXIT_SUCCESS);
        default:
            for (int scnt = 0; scnt < numSigs; scnt++) {
                if (sigsuspend(&emptyMask) == -1 && errno != EINTR) {
                    err_quit("sigsuspend");
                }

                if (kill(childPid, TESTSIG) == -1) {
                    err_quit("kill");
                }
            }

            exit(EXIT_SUCCESS);
    }

    return 0;
}
