#include <inttypes.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>

/*
 * x86-32位系统，设置uint64的变量值不是原子的，信号处理函数
 * 终端程序执行时，可能出现值异常的情况，例如下面的操作
 * 编译: cc -o nonatomic_uint64 -m32 nonatomic_uint64.c
 * 执行: ./nonatomic_uint64
 * Unexpected: ffffffff00000000 (loop 48533)
 * Unexpected: ffffffff00000000 (loop 171488)
 * Unexpected: 00000000ffffffff (loop 272321)
 * Unexpected: 00000000ffffffff (loop 297708)
*/

#define errExit(msg)    do { perror(msg); exit(EXIT_FAILURE); } while (0)

#define SIG SIGUSR1

static volatile uint64_t ip = 0;

static void handler(int sig)
{
    ip = ~ip;
}

int main(int argc, char *argv[])
{
    struct sigaction sa;
    sa.sa_handler = handler;
    sa.sa_flags = 0;
    sigemptyset(&sa.sa_mask);

    if (sigaction(SIG, &sa, NULL) == -1) {
        errExit("sigaction");
    }

    pid_t childPid = fork();
    if (childPid == -1) {
        errExit("fork");
    }

    if (childPid == 0) {
        while (1) {
            if (kill(getppid(), SIG) == -1) {
                errExit("kill() failed in child");
            }
        }
    }

    for (int long long j = 0; ; j++) {
        uint64_t loc = ip;
        if (loc != 0 && loc != ~0) {
            printf("Unexpected: %016" PRIx64 " (loop %lld)\n", loc, j);
        }
    }

    exit(EXIT_SUCCESS);
}

