#include "common.h"
#include <sys/signalfd.h>
#include <signal.h>

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s sig-num...\n", argv[0]);
    }

    printf("%s: PID = %ld\n", argv[0], (long) getpid());
    sigset_t mask = {0};
    sigemptyset(&mask);

    for (int j = 1; j < argc; j++) {
        sigaddset(&mask, atoi(argv[j]));
    }

    if (sigprocmask(SIG_BLOCK, &mask, NULL) == -1) {
        err_quit("sigprocmask");
    }

    int sfd = signalfd(-1, &mask, 0);
    if (sfd == -1) {
        err_quit("signalfd");
    }

    struct signalfd_siginfo fdsi = {0};
    for (;;) {
        ssize_t s = read(sfd, &fdsi, sizeof(struct signalfd_siginfo));
        if (s != sizeof(struct signalfd_siginfo)) {
            err_quit("read");
        }

        printf("%s: got signal %d", argv[0], fdsi.ssi_signo);
        if (fdsi.ssi_code == SI_QUEUE) {
            printf("; ssi_pid = %d; ", fdsi.ssi_pid);
            printf("ssi_int = %d", fdsi.ssi_int);
        }

        printf("\n");
    }
}

