#include "become_daemon.h"
#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>

int becomeDaemon(int flags)
{
    // 变成后台进程，init程序的子进程
    switch (fork()) {
    case -1: return -1;
    case 0:  break;
    default: _exit(EXIT_SUCCESS);
    }

    // 变成会话首进程
    if (setsid() == -1) {
        return -1;
    }

    // 接着创建子进程，确保不是会话首进程
    switch (fork()) {
    case -1: return -1;
    case 0:  break;
    default: _exit(EXIT_SUCCESS);
    }

    if (!(flags & BD_NO_UMASK0)) {
        umask(0);
    }

    if (!(flags & BD_NO_CHDIR)) {
        chdir("/");
    }

    if (!(flags & BD_NO_CLOSE_FILES)) {
        int maxfd = sysconf(_SC_OPEN_MAX);
        if (maxfd == -1) {
            maxfd = BD_MAX_CLOSE;
        }

        for (int fd = 0; fd < maxfd; fd++) {
            close(fd);
        }
    }

    if (!(flags & BD_NO_REOPEN_STD_FDS)) {
        close(STDIN_FILENO);
        int fd = open("/dev/null", O_RDWR);

        if (fd != STDIN_FILENO) {
            return -1;
        }

        if (dup2(STDIN_FILENO, STDOUT_FILENO) != STDOUT_FILENO) {
            return -1;
        }

        if (dup2(STDIN_FILENO, STDERR_FILENO) != STDERR_FILENO) {
            return -1;
        }
    }

    return 0;
}
