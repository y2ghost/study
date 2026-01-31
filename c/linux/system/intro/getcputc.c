#include "common.h"

int main(void)
{
    int c = 0;

    while (1) {
        c = getc(stdin);
        if (EOF == c) {
            break;
        }

        c = putc(c, stdout);
        if (EOF == c) {
            err_sys("output error");
        }
    }

    if (0 != ferror(stdin)) {
        err_sys("input error");
    }

    return 0;
}
