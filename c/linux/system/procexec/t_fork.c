#include "common.h"

static int idata = 111;

int main(int argc, char *argv[])
{
    int istack = 222;
    pid_t childPid = fork();

    switch (childPid) {
    case -1:
        err_quit("fork");
    case 0:
        idata *= 3;
        istack *= 3;
        break;
    default:
        sleep(3);
        break;
    }

    // 父子两个进程都会执行到这
    printf("PID=%ld %s idata=%d istack=%d\n", (long) getpid(),
        (childPid == 0) ? "(child) " : "(parent)", idata, istack);
    exit(EXIT_SUCCESS);
}
