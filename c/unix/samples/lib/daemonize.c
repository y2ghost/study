#include "apue.h"
#include <syslog.h>
#include <fcntl.h>
#include <sys/resource.h>

static void do_fork_(const char *cmd)
{
    pid_t pid = 0;

    pid = fork();
    switch (pid) {
    case -1:
        err_quit("%s: can't fork", cmd);
        break;
    case 0:
        /* child going */
        break;
    default:
        /* parent exit */
        exit(0);
        break;
    }
}

void daemonize(const char *cmd)
{
    int i = 0;
    int rc = 0;
    int fd0 = 0;
    int fd1 = 0;
    int fd2 = 0;
    struct rlimit rl;
    struct sigaction sa;

    /* Clear file creation mask */
    umask(0);
    /* Get maximum number of file descriptors */
    rc = getrlimit(RLIMIT_NOFILE, &rl);
    if (rc < 0) {
        err_quit("%s: can't get file limit", cmd);
    }

    /* Become a session leader to lose controlling TTY */
    do_fork_(cmd);
    setsid();
    sa.sa_handler = SIG_IGN;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = 0;

    rc = sigaction(SIGHUP, &sa, NULL);
    if (rc < 0) {
        err_quit("%s: can't ignore SIGHUP", cmd);
    }

    /* fork again */
    do_fork_(cmd);
    rc = chdir("/");

    if (rc < 0) {
        err_quit("%s: can't change directory to /", cmd);
    }

    if (RLIM_INFINITY == rl.rlim_max) {
        rl.rlim_max = 1024;
    }

    for (i=0; i<rl.rlim_max; i++) {
        close(i);
    }

    /* Attach file descriptors 0, 1, and 2 to /dev/null */
    fd0 = open("/dev/null", O_RDWR);
    fd1 = dup(0);
    fd2 = dup(0);

    /* Initialize the log file */
    openlog(cmd, LOG_CONS, LOG_DAEMON);
    if (0!=fd0 || 1!=fd1 || 2!=fd2) {
        syslog(LOG_ERR, "unexpected file descriptors %d %d %d", fd0, fd1, fd2);
        exit(1);
    }
}
