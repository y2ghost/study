#include "common.h"
#include <string.h>
#include <signal.h>

static void handler(int sig)
{
    printf("PID=%ld: caught signal %d (%s)\n", (long) getpid(),
        sig, strsignal(sig));
}

/*
 * 孤儿进程组示例
 * 执行示例: ./orphaned_pgrp_sighup s p p
 */
int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s {s|p} ...\n", argv[0]);
    }

    setbuf(stdout, NULL);
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = handler;

    if (sigaction(SIGHUP, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    if (sigaction(SIGCONT, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    printf("parent: PID=%ld, PPID=%ld, PGID=%ld, SID=%ld\n",
        (long) getpid(), (long) getppid(),
        (long) getpgrp(), (long) getsid(0));

    for (int j = 1; j < argc; j++) {
        switch (fork()) {
        case -1:
            err_sys("fork");
        case 0:
            printf("child:  PID=%ld, PPID=%ld, PGID=%ld, SID=%ld\n",
                (long) getpid(), (long) getppid(),
                (long) getpgrp(), (long) getsid(0));

            if (argv[j][0] == 's') {
                printf("PID=%ld stopping\n", (long) getpid());
                raise(SIGSTOP);
            } else {
                alarm(60);
                printf("PID=%ld pausing\n", (long) getpid());
                pause();
            }

            _exit(EXIT_SUCCESS);
        default:
            break;
        }
    }

    sleep(3);
    printf("parent exiting\n");
    exit(EXIT_SUCCESS);
}
