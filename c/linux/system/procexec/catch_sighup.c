#include "common.h"
#include <unistd.h>
#include <signal.h>

static void handler(int sig)
{
}

int main(int argc, char *argv[])
{
    setbuf(stdout, NULL);
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(SIGHUP, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    pid_t childPid = fork();
    if (childPid == -1) {
        err_sys("fork");
    }

    if (childPid == 0 && argc > 1) {
        if (setpgid(0, 0) == -1) {
            err_sys("setpgid");
        }
    }

    printf("PID=%ld; PPID=%ld; PGID=%ld; SID=%ld\n", (long) getpid(),
        (long) getppid(), (long) getpgrp(), (long) getsid(0));
    alarm(60);

    for (;;) {
        pause();
        printf("%ld: caught SIGHUP\n", (long) getpid());
    }
}
