#include "common.h"
#include <netdb.h>
#include <errno.h>
#include <syslog.h>
#include <sys/socket.h>

#define BUFLEN  128
#define QLEN    10

#ifndef HOST_NAME_MAX
#define HOST_NAME_MAX 256
#endif

extern int initserver(int, const struct sockaddr *, socklen_t, int);

void serve(int sockfd)
{
    int clfd = 0;
    FILE *fp = NULL;
    char buf[BUFLEN] = {'\0'};

    set_cloexec(sockfd);
    while (1) {
        clfd = accept(sockfd, NULL, NULL);
        if (clfd < 0) {
            syslog(LOG_ERR, "ruptimed: accept error: %s",
              strerror(errno));
            exit(1);
        }

        set_cloexec(clfd);
        fp = popen("/usr/bin/uptime", "r");

        if (NULL == fp) {
            sprintf(buf, "error: %s\n", strerror(errno));
            send(clfd, buf, strlen(buf), 0);
        } else {
            while (1) {
                if (NULL == fgets(buf, BUFLEN, fp)) {
                    break;
                }

                send(clfd, buf, strlen(buf), 0);
            }

            pclose(fp);
        }

        close(clfd);
    }
}

int
main(int argc, char *argv[])
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
        if (sockfd >= 0) {
            serve(sockfd);
            exit(0);
        }
    }

    return 0;
}
