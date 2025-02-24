#include "signal_functions.h"
#include <string.h>
#include <signal.h>

void printSigset(FILE *of, const char *prefix, const sigset_t *sigset)
{
    int cnt = 0;
    for (int sig = 1; sig < NSIG; sig++) {
        if (sigismember(sigset, sig)) {
            cnt++;
            fprintf(of, "%s%d (%s)\n", prefix, sig, strsignal(sig));
        }
    }

    if (cnt == 0) {
        fprintf(of, "%s<empty signal set>\n", prefix);
    }
}

int printSigMask(FILE *of, const char *msg)
{
    if (msg != NULL) {
        fprintf(of, "%s", msg);
    }

    sigset_t currMask;
    if (sigprocmask(SIG_BLOCK, NULL, &currMask) == -1) {
        return -1;
    }

    printSigset(of, "\t\t", &currMask);
    return 0;
}

int printPendingSigs(FILE *of, const char *msg)
{
    if (msg != NULL) {
        fprintf(of, "%s", msg);
    }

    sigset_t pendingSigs;
    if (sigpending(&pendingSigs) == -1) {
        return -1;
    }

    printSigset(of, "\t\t", &pendingSigs);
    return 0;
}

