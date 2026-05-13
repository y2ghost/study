#include <termios.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main(int ac, char *av[])
{
    if (ac>1 && 0==strcmp(av[1], "--help")) {
        fprintf(stderr, "%s [intr-char]\n", av[0]);
        return 1;
    }

    int intr_char = 0;
    if (1 == ac) {
        intr_char = fpathconf(STDIN_FILENO, _PC_VDISABLE);
        if (-1 == intr_char) {
            fprintf(stderr, "%s", "Couldn't determine VDISABLE\n");
            return 1;
        }
    } else if (0 != isdigit(*av[1])) {
        intr_char = strtol(av[1], NULL, 0);
    } else {
        intr_char = *av[1];
    }

    struct termios tp;
    if (-1 == tcgetattr(STDIN_FILENO, &tp)) {
        fprintf(stderr, "%s", "tcgetattr failed!\n");
        return 1;
    }

    tp.c_cc[VINTR] = intr_char;
    if (-1 == tcsetattr(STDIN_FILENO, TCSAFLUSH, &tp)) {
        fprintf(stderr, "%s", "tcsetattr failed!\n");
        return 1;
    }

    return 0;
}
