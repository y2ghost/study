#include "apue.h"
#include <sys/wait.h>

static void sig_cld(int);

int main(void)
{
    if (SIG_ERR == signal(SIGCHLD, sig_cld)) {
        perror("signal error");
    }

    pid_t pid = fork();
    if (pid < 0) {
        perror("fork error");
    } else if (0 == pid) {
        sleep(2);
        _exit(0);
    }

    pause();
    exit(0);
}

static void sig_cld(int signo)
{
    pid_t pid = 0;
    int status = 0;

    printf("SIGCHLD received\n");
    if (SIG_ERR == signal(SIGCHLD, sig_cld)) {
        perror("signal error");
    }

    pid = wait(&status);
    if (pid < 0) {
        perror("wait error");
    }

    printf("pid = %d\n", pid);
}
