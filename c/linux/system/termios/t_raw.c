#include "common.h"

static void sig_catch(int signo)
{
    printf("signal caught\n");
    tty_reset(STDIN_FILENO);
    exit(0);
}

int main(void)
{
    int i= 0;
    char c = '\0';

    if (SIG_ERR == signal(SIGINT, sig_catch)) {
        err_sys("signal(SIGINT) error");
    }

    if (SIG_ERR == signal(SIGQUIT, sig_catch)) {
        err_sys("signal(SIGQUIT) error");
    }

    if (SIG_ERR == signal(SIGTERM, sig_catch)) {
        err_sys("signal(SIGTERM) error");
    }

    if (tty_raw(STDIN_FILENO) < 0) {
        err_sys("tty_raw error");
    }

    printf("Enter raw mode characters, terminate with DELETE\n");
    while (1) {
        i = read(STDIN_FILENO, &c, 1);
        if (1 != i) {
            break;
        }

        if (0177 == (c &= 255)) {
            break;
        }

        printf("%o\n", c);
    }

    if (tty_reset(STDIN_FILENO) < 0) {
        err_sys("tty_reset error");
    }

    if (i <= 0) {
        err_sys("read error");
    }

    if (tty_cbreak(STDIN_FILENO) < 0) {
        err_sys("tty_cbreak error");
    }

    printf("\nEnter cbreak mode characters, terminate with SIGINT\n");
    while (1) {
        i = read(STDIN_FILENO, &c, 1);
        if (1 != i) {
            break;
        }

        c &= 255;
        printf("%o\n", c);
    }

    if (tty_reset(STDIN_FILENO) < 0) {
        err_sys("tty_reset error");
    }

    if (i <= 0) {
        err_sys("read error");
    }

    return 0;
}
