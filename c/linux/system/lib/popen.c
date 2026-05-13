#include "common.h"
#include <errno.h>
#include <fcntl.h>
#include <sys/wait.h>

static int maxfd = 0;
static pid_t *childpid = NULL;

FILE *popen(const char *cmdstring, const char *type)
{
    if (('r'!=type[0] && 'w'!=type[0]) || 0!=type[1]) {
        errno = EINVAL;
        return NULL;
    }

    if (NULL == childpid) {
        maxfd = open_max();
        childpid = calloc(maxfd, sizeof(pid_t));

        if (NULL == childpid) {
            return NULL;
        }
    }

    int pfd[2] = {0, 0};
    if (pipe(pfd) < 0) {
        return NULL;
    }

    if (pfd[0]>=maxfd || pfd[1]>=maxfd) {
        close(pfd[0]);
        close(pfd[1]);
        errno = EMFILE;
        return NULL;
    }

    pid_t pid = fork();
    switch (pid) {
    case -1:
        return NULL;
        break;
    case 0:
        if ('r' == *type) {
            close(pfd[0]);
            if (STDOUT_FILENO != pfd[1]) {
                dup2(pfd[1], STDOUT_FILENO);
                close(pfd[1]);
            }
        } else {
            close(pfd[1]);
            if (STDIN_FILENO != pfd[0]) {
                dup2(pfd[0], STDIN_FILENO);
                close(pfd[0]);
            }
        }

        for (int i=0; i<maxfd; i++) {
            if (childpid[i] > 0) {
                close(i);
            }
        }

        execl("/bin/sh", "sh", "-c", cmdstring, (char *)0);
        _exit(127);
        break;
    default:
        break;
    }
    
    FILE *fp = NULL;
    if ('r' == *type) {
        close(pfd[1]);
        fp = fdopen(pfd[0], type);

        if (NULL == fp) {
            return NULL;
        }
    } else {
        close(pfd[0]);
        fp = fdopen(pfd[1], type);

        if (NULL == fp) {
            return NULL;
        }
    }

    childpid[fileno(fp)] = pid;
    return fp;
}

int pclose(FILE *fp)
{
    if (NULL == childpid) {
        errno = EINVAL;
        return -1;
    }

    int fd = fileno(fp);
    if (fd >= maxfd) {
        errno = EINVAL;
        return -1;
    }
    
    pid_t pid = childpid[fd];
    if (0 == pid) {
        errno = EINVAL;
        return -1;
    }

    childpid[fd] = 0;
    if (EOF == fclose(fp)) {
        return -1;
    }

    int stat = 0;
    while (waitpid(pid, &stat, 0) < 0) {
        if (EINTR != errno) {
            return -1;
        }
    }

    return stat;
}

