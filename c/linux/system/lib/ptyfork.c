#include "common.h"
#include <termios.h>

pid_t pty_fork(int *ptrfdm, char *slave_name, int slave_namesz,
    const struct termios *slave_termios,
    const struct winsize *slave_winsize)
{
    int fdm = 0;
    char pts_name[20] = {'\0'};

    if ((fdm = ptym_open(pts_name,sizeof(pts_name))) < 0) {
        err_sys("can't open master pty: %s, error %d", pts_name, fdm);
    }

    if (NULL != slave_name) {
        strncpy(slave_name, pts_name, slave_namesz);
        slave_name[slave_namesz - 1] = '\0';
    }

    pid_t pid = fork();
    switch (pid) {
    case -1:
        return -1;
        break;
    case 0:
        if (setsid() < 0) {
            err_sys("setsid error");
        }

        int fds = ptys_open(pts_name);
        if (fds < 0) {
            err_sys("can't open slave pty");
        }

        close(fdm);
        if (NULL != slave_termios) {
            if (tcsetattr(fds,TCSANOW,slave_termios) < 0) {
                err_sys("tcsetattr error on slave pty");
            }
        }

        if (NULL != slave_winsize) {
            if (ioctl(fds,TIOCSWINSZ,slave_winsize) < 0) {
                err_sys("TIOCSWINSZ error on slave pty");
            }
        }

        if (STDIN_FILENO != dup2(fds, STDIN_FILENO)) {
            err_sys("dup2 error to stdin");
        }

        if (STDOUT_FILENO != dup2(fds, STDOUT_FILENO)) {
            err_sys("dup2 error to stdout");
        }

        if (STDERR_FILENO != dup2(fds, STDERR_FILENO)) {
            err_sys("dup2 error to stderr");
        }

        if (STDIN_FILENO!=fds && STDOUT_FILENO!=fds && STDERR_FILENO!=fds) {
            close(fds);
        }

        return 0;
        break;
    default:
        break;
    }
    
    *ptrfdm = fdm;
    return pid;
}
