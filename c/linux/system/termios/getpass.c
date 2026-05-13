#include <signal.h>
#include <stdio.h>
#include <termios.h>

#define MAX_PASS_LEN    8

char *getpass(const char *prompt)
{
    static char buf[MAX_PASS_LEN + 1] = {'\0'};
    char *ptr = NULL;
    sigset_t sig;
    sigset_t osig;
    struct termios ts;
    struct termios ots;
    FILE *fp = NULL;
    int c = 0;

    fp = fopen(ctermid(NULL), "r+");
    if (NULL == fp) {
        return NULL;
    }

    setbuf(fp, NULL);
    sigemptyset(&sig);
    sigaddset(&sig, SIGINT);
    sigaddset(&sig, SIGTSTP);
    sigprocmask(SIG_BLOCK, &sig, &osig);
    tcgetattr(fileno(fp), &ts);
    ots = ts;
    ts.c_lflag &= ~(ECHO | ECHOE | ECHOK | ECHONL);
    tcsetattr(fileno(fp), TCSAFLUSH, &ts);
    fputs(prompt, fp);

    ptr = buf;
    while (1) {
        c = getc(fp);
        if (EOF==c || '\n'==c) {
            break;
        }

        if (ptr < &buf[MAX_PASS_LEN]) {
            *ptr++ = c;
        }
    }

    *ptr = 0;
    putc('\n', fp);
    tcsetattr(fileno(fp), TCSAFLUSH, &ots);
    sigprocmask(SIG_SETMASK, &osig, NULL);
    fclose(fp);
    return buf;
}
