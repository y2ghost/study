#include "common.h"
#include <sys/wait.h>

static void sig_int(int);

int main(void)
{
    pid_t pid = 0;
    int status = 0;
    char buf[MAXLINE] = {'\0'};
    void (*sig_handler)(int) = NULL;

    sig_handler = signal(SIGINT, sig_int);
    if (SIG_ERR == sig_handler) {
        err_sys("signal error");
    }

    printf("%% ");
    while (1) {
        int last = 0;
        char *line = NULL;
        
        line = fgets(buf, MAXLINE, stdin);
        if (NULL == line) {
            break;
        }

        last = strlen(line) - 1;
        if ('\n' == line[last]) {
            line[last] = '\0';
        }

        pid = fork();
        switch (pid) {
            case -1:
                err_sys("fork error");
                break;
            case 0:
                execlp(line, line, NULL);
                err_ret("couldn't execute: %s", line);
                exit(127);
                break;
            default:
                /* parent */
                pid = waitpid(pid, &status, 0);
                if (pid < 0) {
                    err_sys("waitpid error");
                }
                
                printf("%% ");
        }
    }

    return 0;
}

void sig_int(int signo)
{
    printf("interrupt\n%% ");
}
