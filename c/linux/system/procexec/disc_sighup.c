#include "common.h"
#include <string.h>
#include <signal.h>

static void handler(int sig)
{
    printf("PID %ld: caught signal %2d (%s)\n", (long) getpid(),
        sig, strsignal(sig));
}

// 执行示例: exec ./disc_sighup d s s > sig.log 2>&1
int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s {d|s}... [ > sig.log 2>&1 ]\n", argv[0]);
    }

    setbuf(stdout, NULL);
    pid_t parentPid = getpid();
    printf("PID of parent process is:       %ld\n", (long) parentPid);
    printf("Foreground process group ID is: %ld\n",
        (long) tcgetpgrp(STDIN_FILENO));

    for (int j = 1; j < argc; j++) {
        pid_t childPid = fork();
        if (childPid == -1) {
            err_sys("fork");
        }

        if (childPid == 0) {
            if (argv[j][0] == 'd') {
                if (setpgid(0, 0) == -1) {
                    err_sys("setpgid");
                }
            }

            struct sigaction sa;
            sigemptyset(&sa.sa_mask);
            sa.sa_flags = 0;
            sa.sa_handler = handler;

            if (sigaction(SIGHUP, &sa, NULL) == -1) {
                err_sys("sigaction");
            }

            break;
        }
    }

    alarm(60);
    printf("PID=%ld PGID=%ld\n", (long) getpid(), (long) getpgrp());

    for (;;) {
        pause();
    }
}
