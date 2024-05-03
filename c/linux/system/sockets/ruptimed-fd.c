#include "common.h"
#include <netdb.h>
#include <errno.h>
#include <syslog.h>
#include <fcntl.h>
#include <sys/socket.h>
#include <sys/wait.h>

#define QLEN 10

#ifndef HOST_NAME_MAX
#define HOST_NAME_MAX 256
#endif

extern int initserver(int, const struct sockaddr *, socklen_t, int);

void serve(int sockfd)
{
    int clfd = 0;
    int status = 0;
    pid_t pid = 0;

    set_cloexec(sockfd);
    while (1) {
        clfd = accept(sockfd, NULL, NULL);
        if (clfd < 0) {
            syslog(LOG_ERR, "ruptimed: accept error: %s",
              strerror(errno));
            exit(1);
        }

        pid = fork();
        if (pid < 0) {
            syslog(LOG_ERR, "ruptimed: fork error: %s",
              strerror(errno));
            exit(1);
        } else if (0 == pid) {
            int fd0 = 0;
            int fd1 = 0;
            /*
             * The parent called daemonize ({Prog daemoninit}), so
             * STDIN_FILENO, STDOUT_FILENO, and STDERR_FILENO
             * are already open to /dev/null.  Thus, the call to
             * close doesn't need to be protected by checks that
             * clfd isn't already equal to one of these values.
             */
            fd0 = dup2(clfd, STDOUT_FILENO);
            fd1 = dup2(clfd, STDERR_FILENO);
            if (STDOUT_FILENO!=fd0 || STDERR_FILENO!=fd1) {
                syslog(LOG_ERR, "ruptimed: unexpected error");
                exit(1);
            }

            close(clfd);
            execl("/usr/bin/uptime", "uptime", (char *)0);
            syslog(LOG_ERR, "ruptimed: unexpected return from exec: %s",
                strerror(errno));
        } else {
            close(clfd);
            waitpid(pid, &status, 0);
        }
    }
}

int main(int argc, char *argv[])
{
    struct addrinfo *aip = NULL;
    struct addrinfo *ailist = NULL;
    struct addrinfo hint;
    int sockfd = 0;
    int err = 0;
    int n = 0;
    char *host = NULL;

    if (1 != argc) {
        err_quit("usage: ruptimed");
    }

    n = sysconf(_SC_HOST_NAME_MAX);
    if (n < 0) {
        n = HOST_NAME_MAX;
    }

    host = malloc(n);
    if (NULL == host) {
        err_sys("malloc error");
    }

    if (gethostname(host, n) < 0) {
        err_sys("gethostname error");
    }

    daemonize("ruptimed");
    memset(&hint, 0, sizeof(hint));
    hint.ai_flags = AI_CANONNAME;
    hint.ai_socktype = SOCK_STREAM;
    hint.ai_canonname = NULL;
    hint.ai_addr = NULL;
    hint.ai_next = NULL;

    err = getaddrinfo(host, "ruptime", &hint, &ailist);
    if (0 != err) {
        syslog(LOG_ERR, "ruptimed: getaddrinfo error: %s",
          gai_strerror(err));
        exit(1);
    }

    for (aip=ailist; NULL!=aip; aip=aip->ai_next) {
        sockfd = initserver(SOCK_STREAM, aip->ai_addr, aip->ai_addrlen, QLEN);
        if (sockfd < 0) {
            serve(sockfd);
            exit(0);
        }
    }

    return 1;
}
