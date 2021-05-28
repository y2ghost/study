#include "apue.h"

static void sig_alrm(int);

int main(void)
{
    int n = 0;
    char line[MAXLINE] = {'\0'};

    if (SIG_ERR == signal(SIGALRM, sig_alrm)) {
        err_sys("signal(SIGALRM) error");
    }

    alarm(10);
    n = read(STDIN_FILENO, line, MAXLINE);

    if (n < 0) {
        err_sys("read error");
    }

    alarm(0);
    write(STDOUT_FILENO, line, n);
    exit(0);
}

static void sig_alrm(int signo)
{
    /* nothing to do, just return to interrupt the read */
}
