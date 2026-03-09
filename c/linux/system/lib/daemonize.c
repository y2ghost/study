#include "common.h"
#include <syslog.h>
#include <fcntl.h>
#include <sys/resource.h>

static void do_fork_(const char *cmd)
{
    pid_t pid = fork();
    switch (pid) {
    case -1:
        err_quit("%s: can't fork", cmd);
        break;
    case 0:
        /* 子进程继续 */
        break;
    default:
        /* 父进程退出 */
        exit(0);
        break;
    }
}

void daemonize(const char *cmd)
{
    struct rlimit rl;
    /* 重置文件创建掩码 */
    umask(0);
    /* 获取最大文件描述符 */
    int rc = getrlimit(RLIMIT_NOFILE, &rl);
    if (rc < 0) {
        err_quit("%s: can't get file limit", cmd);
    }

    // 变成SESSION LEADER，关闭 TTY
    do_fork_(cmd);
    setsid();

    struct sigaction sa;
    sa.sa_handler = SIG_IGN;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;

    rc = sigaction(SIGHUP, &sa, NULL);
    if (rc < 0) {
        err_quit("%s: can't ignore SIGHUP", cmd);
    }

    do_fork_(cmd);
    rc = chdir("/");

    if (rc < 0) {
        err_quit("%s: can't change directory to /", cmd);
    }

    if (RLIM_INFINITY == rl.rlim_max) {
        rl.rlim_max = 1024;
    }

    for (int i=0; i<rl.rlim_max; i++) {
        close(i);
    }

    int fd0 = open("/dev/null", O_RDWR);
    int fd1 = dup(0);
    int fd2 = dup(0);

    openlog(cmd, LOG_CONS, LOG_DAEMON);
    if (0!=fd0 || 1!=fd1 || 2!=fd2) {
        syslog(LOG_ERR, "unexpected file descriptors %d %d %d", fd0, fd1, fd2);
        exit(1);
    }
}
