#include "print_wait_status.h"
#include "curr_time.h"
#include "common.h"
#include <signal.h>
#include <sys/wait.h>
#include <errno.h>

static volatile int numLiveChildren = 0;

static void sigchldHandler(int sig)
{
    int status = 0;
    int savedErrno = errno;
    pid_t childPid;
    printf("%s handler: Caught SIGCHLD\n", currTime("%T"));

    while ((childPid = waitpid(-1, &status, WNOHANG)) > 0) {
        printf("%s handler: Reaped child %ld - ", currTime("%T"),
            (long) childPid);
        printWaitStatus(NULL, status);
        numLiveChildren--;
    }

    if (childPid == -1 && errno != ECHILD) {
        err_quit("waitpid");
    }

    sleep(5);
    printf("%s handler: returning\n", currTime("%T"));
    errno = savedErrno;
}

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s child-sleep-time...\n", argv[0]);
    }

    setbuf(stdout, NULL);
    int sigCnt = 0;
    numLiveChildren = argc - 1;
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;
    sa.sa_handler = sigchldHandler;

    if (sigaction(SIGCHLD, &sa, NULL) == -1) {
        err_quit("sigaction");
    }


    sigset_t blockMask;
    sigemptyset(&blockMask);
    sigaddset(&blockMask, SIGCHLD);

    if (sigprocmask(SIG_SETMASK, &blockMask, NULL) == -1) {
        err_quit("sigprocmask");
    }

    for (int j = 1; j < argc; j++) {
        switch (fork()) {
        case -1:
            err_quit("fork");
        case 0:
            sleep(atoi(argv[j]));
            printf("%s Child %d (PID=%ld) exiting\n", currTime("%T"),
                j, (long) getpid());
            _exit(EXIT_SUCCESS);
        default:
            break;
        }
    }

    sigset_t emptyMask;
    sigemptyset(&emptyMask);

    while (numLiveChildren > 0) {
        if (sigsuspend(&emptyMask) == -1 && errno != EINTR) {
            err_quit("sigsuspend");
        }

        sigCnt++;
    }

    printf("%s All %d children have terminated; SIGCHLD was caught "
        "%d times\n", currTime("%T"), argc - 1, sigCnt);
    exit(EXIT_SUCCESS);
}

