#include "common.h"
#include <termios.h>
#include <sys/ioctl.h>

static void pr_winsize(int fd)
{
    struct winsize size;

    if (ioctl(fd,TIOCGWINSZ,(char *) &size) < 0) {
        err_sys("TIOCGWINSZ error");
    }

    printf("%d rows, %d columns\n", size.ws_row, size.ws_col);
}

static void sig_winch(int signo)
{
    printf("SIGWINCH received\n");
    pr_winsize(STDIN_FILENO);
}

int main(void)
{
    if (0 == isatty(STDIN_FILENO)) {
        exit(1);
    }

    if (SIG_ERR == signal(SIGWINCH, sig_winch)) {
        err_sys("signal error");
    }

    pr_winsize(STDIN_FILENO);
    while (1) {
        pause();
    }

    return 0;
}
