#include "common.h"
#include <signal.h>
#include <errno.h>

static void tstpHandler(int sig)
{

    int savedErrno = errno;
    printf("Caught SIGTSTP\n");

    // 设置默认处理器
    if (signal(SIGTSTP, SIG_DFL) == SIG_ERR) {
        err_sys("signal");
    }

    // 生成SIGTSTP信息
    raise(SIGTSTP);
    // 不阻塞SIGTSTP信息
    sigset_t tstpMask, prevMask;
    sigemptyset(&tstpMask);
    sigaddset(&tstpMask, SIGTSTP);

    if (sigprocmask(SIG_UNBLOCK, &tstpMask, &prevMask) == -1) {
        err_sys("sigprocmask");
    }

    if (sigprocmask(SIG_SETMASK, &prevMask, NULL) == -1) {
        err_sys("sigprocmask");
    }

    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    sa.sa_handler = tstpHandler;

    if (sigaction(SIGTSTP, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    printf("Exiting SIGTSTP handler\n");
    errno = savedErrno;
}

int main(int argc, char *argv[])
{
    struct sigaction sa;
    if (sigaction(SIGTSTP, NULL, &sa) == -1) {
        err_sys("sigaction");
    }

    if (sa.sa_handler != SIG_IGN) {
        sigemptyset(&sa.sa_mask);
        sa.sa_flags = SA_RESTART;
        sa.sa_handler = tstpHandler;

        if (sigaction(SIGTSTP, &sa, NULL) == -1) {
            err_sys("sigaction");
        }
    }

    for (;;) {
        pause();
        printf("Main\n");
    }
}
