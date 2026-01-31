#include "curr_time.h"
#include "common.h"
#include <signal.h>
#include <errno.h>

#define SYNC_SIG SIGUSR1

static void handler(int sig)
{
}

// 父子进程使用信号同步
int main(int argc, char *argv[])
{
    setbuf(stdout, NULL);
    sigset_t blockMask;
    sigset_t origMask;
    sigemptyset(&blockMask);
    sigaddset(&blockMask, SYNC_SIG);

    if (sigprocmask(SIG_BLOCK, &blockMask, &origMask) == -1) {
        err_quit("sigprocmask");
    }

    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    sa.sa_handler = handler;

    if (sigaction(SYNC_SIG, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    pid_t childPid = fork();
    switch (childPid) {
    case -1:
        err_quit("fork");
    case 0:
        printf("[%s %ld] Child started - doing some work\n",
            currTime("%T"), (long) getpid());
        sleep(2);
        printf("[%s %ld] Child about to signal parent\n",
            currTime("%T"), (long) getpid());

        if (kill(getppid(), SYNC_SIG) == -1) {
            err_quit("kill");
        }

        _exit(EXIT_SUCCESS);
    default:
        printf("[%s %ld] Parent about to wait for signal\n",
            currTime("%T"), (long) getpid());
        sigset_t emptyMask;
        sigemptyset(&emptyMask);

        if (sigsuspend(&emptyMask) == -1 && errno != EINTR) {
            err_quit("sigsuspend");
        }

        printf("[%s %ld] Parent got signal\n", currTime("%T"), (long) getpid());
        if (sigprocmask(SIG_SETMASK, &origMask, NULL) == -1) {
            err_quit("sigprocmask");
        }

        exit(EXIT_SUCCESS);
    }

    return 0;
}

