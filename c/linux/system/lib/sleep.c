#include "common.h"

static void sig_alrm(int signo)
{
    // 仅仅用于唤醒sigsuspend
}

unsigned int sleep(unsigned int seconds)
{
    struct sigaction newact;
    struct sigaction oldact;
    sigset_t newmask;
    sigset_t oldmask;
    sigset_t suspmask;
    unsigned int unslept = 0;

    // 设置新的处理器并保存旧的
    newact.sa_handler = sig_alrm;
    sigemptyset(&newact.sa_mask);
    newact.sa_flags = 0;
    sigaction(SIGALRM, &newact, &oldact);

    // 阻塞SIGALRM信息
    sigemptyset(&newmask);
    sigaddset(&newmask, SIGALRM);
    sigprocmask(SIG_BLOCK, &newmask, &oldmask);

    alarm(seconds);
    suspmask = oldmask;

    sigdelset(&suspmask, SIGALRM);
    sigsuspend(&suspmask);
    unslept = alarm(0);
    sigaction(SIGALRM, &oldact, NULL);
    sigprocmask(SIG_SETMASK, &oldmask, NULL);
    return unslept;
}

