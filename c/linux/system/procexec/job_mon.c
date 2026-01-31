#include "common.h"
#include <string.h>
#include <signal.h>
#include <fcntl.h>

static int cmdNum = 0;

static void handler(int sig)
{
    if (getpid() == getpgrp()) {
        fprintf(stderr, "Terminal FG process group: %ld\n",
            (long) tcgetpgrp(STDERR_FILENO));
    }

    fprintf(stderr, "Process %ld (%d) received signal %d (%s)\n",
        (long) getpid(), cmdNum, sig, strsignal(sig));
    if (sig == SIGTSTP) {
        raise(SIGSTOP);
    }
}

// 演示作业控制: ./job_mon | ./job_mon | ./job_mon
int main(int argc, char *argv[])
{
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    sa.sa_handler = handler;

    if (sigaction(SIGINT, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    if (sigaction(SIGTSTP, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    if (sigaction(SIGCONT, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    if (isatty(STDIN_FILENO)) {
        fprintf(stderr, "Terminal FG process group: %ld\n",
            (long) tcgetpgrp(STDIN_FILENO));
        fprintf(stderr, "Command   PID  PPID  PGRP   SID\n");
        cmdNum = 0;
    } else {
        if (read(STDIN_FILENO, &cmdNum, sizeof(cmdNum)) <= 0) {
            err_quit("read got EOF or error");
        }
    }

    cmdNum++;
    fprintf(stderr, "%4d    %5ld %5ld %5ld %5ld\n", cmdNum,
        (long) getpid(), (long) getppid(),
            (long) getpgrp(), (long) getsid(0));

    if (!isatty(STDOUT_FILENO)) {
        if (write(STDOUT_FILENO, &cmdNum, sizeof(cmdNum)) == -1) {
            err_msg("write");
        }
    }

    for (;;) {
        pause();
    }
}

