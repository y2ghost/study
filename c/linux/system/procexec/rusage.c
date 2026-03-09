#include "print_rusage.h"
#include "common.h"
#include <sys/resource.h>
#include <sys/wait.h>

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s command arg...\n", argv[0]);
    }

    pid_t childPid = fork();
    switch (childPid) {
    case -1:
        err_sys("fork");
    case 0:
        execvp(argv[1], &argv[1]);
        err_sys("execvp");
    default:
        printf("Command PID: %ld\n", (long) childPid);
        if (wait(NULL) == -1) {
            err_sys("wait");
        }

        struct rusage ru;
        if (getrusage(RUSAGE_CHILDREN, &ru) == -1) {
            err_sys("getrusage");
        }

        printf("\n");
        printRusage("\t", &ru);
        exit(EXIT_SUCCESS);
    }
}
