#include "common.h"

extern char **environ;

int main(int argc, char *argv[])
{
    for (int j = 0; j < argc; j++) {
        printf("argv[%d] = %s\n", j, argv[j]);
    }

    for (char **ep = environ; *ep != NULL; ep++) {
        printf("environ: %s\n", *ep);
    }

    exit(EXIT_SUCCESS);
}
