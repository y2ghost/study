#include "common.h"
#include <string.h>
#include <signal.h>

static void sigsegvHandler(int sig)
{
    int x = 0;
    printf("Caught signal %d (%s)\n", sig, strsignal(sig));
    printf("Top of handler stack near     %10p\n", (void *) &x);
    fflush(NULL);
    _exit(EXIT_FAILURE);
}

#ifdef __GNUC__
static void overflowStack(int callNum) __attribute__ ((__noreturn__));
#endif

static void overflowStack(int callNum)
{
    char a[100000] = {0};
    printf("Call %4d - top of stack near %10p\n", callNum, &a[0]);
    overflowStack(callNum+1);
}

int main(int argc, char *argv[])
{
    int j = 0;
    printf("Top of standard stack is near %10p\n", (void *) &j);

    stack_t sigstack = {0};
    sigstack.ss_sp = malloc(SIGSTKSZ);

    if (sigstack.ss_sp == NULL) {
        err_quit("malloc");
    }

    sigstack.ss_size = SIGSTKSZ;
    sigstack.ss_flags = 0;

    if (sigaltstack(&sigstack, NULL) == -1) {
        err_quit("sigaltstack");
    }

    printf("Alternate stack is at         %10p-%p\n",
            sigstack.ss_sp, (char *) sbrk(0) - 1);
    struct sigaction sa = {0};
    sa.sa_handler = sigsegvHandler;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_ONSTACK;

    if (sigaction(SIGSEGV, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    overflowStack(1);
}

