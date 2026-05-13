#include "common.h"
#include <signal.h>
#include <errno.h>

#define BUF_SIZE 200

static void handler(int sig)
{
    printf("Caught signal\n");
}

int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [num-secs [restart-flag]]\n", argv[0]);
    }

    struct sigaction sa;
    sa.sa_flags = (argc > 2) ? SA_RESTART : 0;
    sigemptyset(&sa.sa_mask);
    sa.sa_handler = handler;

    if (sigaction(SIGALRM, &sa, NULL) == -1) {
        err_quit("sigaction");
    }

    char buf[BUF_SIZE] = {0};
    alarm((argc > 1) ? atoi(argv[1]) : 10);
    ssize_t numRead = read(STDIN_FILENO, buf, BUF_SIZE);
    int savedErrno = errno;
    alarm(0);
    errno = savedErrno;

    if (numRead == -1) {
        if (errno == EINTR) {
            printf("Read timed out\n");
        } else {
            err_msg("read");
        }
    } else {
        printf("Successful read (%ld bytes): %.*s",
            (long) numRead, (int) numRead, buf);
    }

    exit(EXIT_SUCCESS);
}
