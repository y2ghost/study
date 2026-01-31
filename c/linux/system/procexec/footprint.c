#include "common.h"
#include <sys/wait.h>

static int func(int arg)
{
    for (int j = 0; j < 0x100; j++) {
        if (malloc(0x8000) == NULL) {
            err_quit("malloc");
        }
    }

    printf("Program break in child:  %10p\n", sbrk(0));
    return arg;
}

int main(int argc, char *argv[])
{
    setbuf(stdout, NULL);
    printf("Program break in parent: %10p\n", sbrk(0));
    pid_t childPid = fork();

    if (childPid == -1) {
        err_quit("fork");
    }

    int arg = (argc > 1) ? atoi(argv[1]) : 0;
    if (childPid == 0) {
        exit(func(arg));
    }

    int status = 0;
    if (wait(&status) == -1) {
        err_quit("wait");
    }

    printf("Program break in parent: %10p\n", sbrk(0));
    printf("Status = %d %d\n", status, WEXITSTATUS(status));
    exit(EXIT_SUCCESS);
}

