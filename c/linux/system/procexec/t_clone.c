#include "common.h"
#include <signal.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sched.h>
#include <errno.h>

#ifndef CHILD_SIG
#define CHILD_SIG SIGUSR1
#endif

static int childFunc(void *arg)
{
    if (close(*((int *) arg)) == -1) {
        err_quit("close");
    }

    return 0;
}

int main(int argc, char *argv[])
{
    const int STACK_SIZE = 65536;
    int fd = open("/dev/null", O_RDWR);

    if (fd == -1) {
        err_quit("open");
    }

    int flags = (argc > 1) ? CLONE_FILES : 0;
    char *stack = malloc(STACK_SIZE);

    if (stack == NULL) {
        err_quit("malloc");
    }

    char *stackTop = stack + STACK_SIZE;
    if (CHILD_SIG != 0 && CHILD_SIG != SIGCHLD) {
        if (signal(CHILD_SIG, SIG_IGN) == SIG_ERR) {
            err_quit("signal");
        }
    }

    if (clone(childFunc, stackTop, flags | CHILD_SIG, (void *) &fd) == -1) {
        err_quit("clone");
    }

    if (waitpid(-1, NULL, (CHILD_SIG != SIGCHLD) ? __WCLONE : 0) == -1) {
        err_quit("waitpid");
    }

    printf("child has terminated\n");
    int s = write(fd, "x", 1);

    if (s == -1 && errno == EBADF) {
        printf("file descriptor %d has been closed\n", fd);
    } else if (s == -1) {
        printf("write() on file descriptor %d failed "
            "unexpectedly (%s)\n", fd, strerror(errno));
    } else {
        printf("write() on file descriptor %d succeeded\n", fd);
    }

    exit(EXIT_SUCCESS);
}
