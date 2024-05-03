#include "signal_functions.h"
#include "common.h"
#include <string.h>
#include <setjmp.h>
#include <signal.h>

static volatile sig_atomic_t canJump = 0;
static jmp_buf env;

static void handler(int sig)
{
    printf("Received signal %d (%s), signal mask is:\n", sig,
            strsignal(sig));
    printSigMask(stdout, NULL);

    if (!canJump) {
        printf("'env' buffer not yet set, doing a simple return\n");
        return;
    }

    longjmp(env, 1);
}

int main(int argc, char *argv[])
{
    struct sigaction sa;
    printSigMask(stdout, "Signal mask at startup:\n");
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    printf("Calling setjmp()\n");
    if (setjmp(env) == 0) {
        canJump = 1;
    } else {
        printSigMask(stdout, "After jump from handler, signal mask is:\n" );
    }

    for (;;) {
        pause();
    }

    return 0;
}
