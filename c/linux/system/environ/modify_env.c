#include "common.h"
#include <stdlib.h>

extern char **environ;

int main(int argc, char *argv[])
{
    for (int j = 1; j < argc; j++) {
        if (putenv(argv[j]) != 0) {
            err_quit("putenv: %s", argv[j]);
        }
    }

    if (setenv("GREET", "Hello world", 0) == -1) {
        err_quit("setenv");
    }

    unsetenv("BYE");
    for (char **ep = environ; *ep != NULL; ep++) {
        puts(*ep);
    }

    exit(EXIT_SUCCESS);
}

