#include "common.h"
#include <sys/wait.h>
#include <signal.h>
#include <time.h>
#include <sys/resource.h>

#define NSECS 3
#define SIG SIGUSR1

static void handler(int sig)
{
}

static void printChildRusage(const char *msg)
{
    struct rusage ru;
    printf("%s", msg);

    if (getrusage(RUSAGE_CHILDREN, &ru) == -1) {
        err_sys("getrusage");
    }

    printf("user CPU=%.2f secs; system CPU=%.2f secs\n",
        ru.ru_utime.tv_sec + ru.ru_utime.tv_usec / 1000000.0,
        ru.ru_stime.tv_sec + ru.ru_stime.tv_usec / 1000000.0);
}

int main(int argc, char *argv[])
{
    setbuf(stdout, NULL);
    struct sigaction sa;
    sa.sa_handler = handler;
    sa.sa_flags = 0;
    sigemptyset(&sa.sa_mask);

    if (sigaction(SIG, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    sigset_t mask;
    sigemptyset(&mask);
    sigaddset(&mask, SIG);

    if (sigprocmask(SIG_BLOCK, &mask, NULL) == -1) {
        err_sys("sigprocmask");
    }

    switch (fork()) {
    case -1:
        err_sys("fork");
    case 0:
        for (clock_t start = clock(); clock() - start < NSECS * CLOCKS_PER_SEC;) {
            continue;
        }

        printf("Child terminating\n");
        if (kill(getppid(), SIG) == -1) {
            err_sys("kill");
        }

        exit(EXIT_SUCCESS);
    default:
        sigemptyset(&mask);
        sigsuspend(&mask);
        sleep(2);
        printChildRusage("Before wait: ");

        if (wait(NULL) == -1) {
            err_sys("wait");
        }

        printChildRusage("After wait:  ");
        exit(EXIT_SUCCESS);
    }
}
