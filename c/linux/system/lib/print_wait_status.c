#include "print_wait_status.h"
#include "common.h"
#include <string.h>
#include <sys/wait.h>

void printWaitStatus(const char *msg, int status)
{
    if (msg != NULL) {
        printf("%s", msg);
    }

    if (WIFEXITED(status)) {
        printf("child exited, status=%d\n", WEXITSTATUS(status));
    } else if (WIFSIGNALED(status)) {
        printf("child killed by signal %d (%s)",
            WTERMSIG(status), strsignal(WTERMSIG(status)));
        if (WCOREDUMP(status)) {
            printf(" (core dumped)");
        }

        printf("\n");
    } else if (WIFSTOPPED(status)) {
        printf("child stopped by signal %d (%s)\n",
            WSTOPSIG(status), strsignal(WSTOPSIG(status)));
    } else if (WIFCONTINUED(status)) {
        printf("child continued\n");
    } else {
        printf("what happened to this child? (status=%x)\n",
            (unsigned int) status);
    }
}

