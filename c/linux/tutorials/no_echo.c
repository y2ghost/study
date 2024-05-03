#include <termios.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main(int ac, char *av[])
{
    struct termios tp;
    if (-1 == tcgetattr(STDIN_FILENO, &tp)) {
        fprintf(stderr, "%s", "tcgetattr failed\n");
        return 1;
    }

    struct termios save = tp;
    tp.c_lflag &= ~ECHO;

    if (-1 == tcsetattr(STDIN_FILENO, TCSAFLUSH, &tp)) {
        fprintf(stderr, "%s", "tcsetattr failed\n");
        return 1;
    }

    printf("Enter text: ");
    fflush(stdout);

    char buf[512];
    if (NULL == fgets(buf, sizeof(buf), stdin)) {
        fprintf(stderr, "%s", "Got end-of-file/error on fgets()\n");
    } else {
        printf("\nRead: %s", buf);
    }

    if (-1 == tcsetattr(STDIN_FILENO, TCSANOW, &save)) {
        fprintf(stderr, "%s", "tcsetattr failed!\n");
        return 1;
    }

    return 0;
}



