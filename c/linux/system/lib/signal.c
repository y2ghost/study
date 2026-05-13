#include "common.h"

sigfunc *signal(int signo, sigfunc *func)
{
    struct sigaction act;
    struct sigaction oact;

    memset(&act, 0x0, sizeof(act));
    memset(&oact, 0x0, sizeof(oact));
    act.sa_handler = func;
    sigemptyset(&act.sa_mask);
    act.sa_flags = 0;
    act.sa_flags |= SA_RESTART;

    if (sigaction(signo, &act, &oact) < 0) {
        return SIG_ERR;
    }

    return oact.sa_handler;
}
