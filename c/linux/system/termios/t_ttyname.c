#include "common.h"

int main(void)
{
    char *name = NULL;

    if (isatty(0)) {
        name = ttyname(0);
        if (NULL == name) {
            name = "undefined";
        }
    } else {
        name = "not a tty";
    }

    printf("fd 0: %s\n", name);
    if (isatty(1)) {
        name = ttyname(1);
        if (NULL == name) {
            name = "undefined";
        }
    } else {
        name = "not a tty";
    }

    printf("fd 1: %s\n", name);
    if (isatty(2)) {
        name = ttyname(2);
        if (NULL == name) {
            name = "undefined";
        }
    } else {
        name = "not a tty";
    }

    printf("fd 2: %s\n", name);
    return 0;
}
