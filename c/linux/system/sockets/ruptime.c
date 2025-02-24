#include "common.h"
#include <netdb.h>
#include <errno.h>
#include <sys/socket.h>

#define BUFLEN  128

extern int connect_retry(int, int, int, const struct sockaddr *, socklen_t);

void print_uptime(int sockfd)
{
    int n = 0;
    char buf[BUFLEN] = {'\0'};

    while (1) {
        n = recv(sockfd, buf, BUFLEN, 0);
        if (n <= 0) {
            break;
        }

        write(STDOUT_FILENO, buf, n);
    }

    if (n < 0) {
        err_sys("recv error");
    }
}

int main(int argc, char *argv[])
{
    struct addrinfo *aip = NULL;
    struct addrinfo *ailist = NULL;
    struct addrinfo hint;
    int sockfd = 0;
    int err = 0;

    if (2 != argc) {
        err_quit("usage: ruptime hostname");
    }

    memset(&hint, 0, sizeof(hint));
    hint.ai_socktype = SOCK_STREAM;
    hint.ai_canonname = NULL;
    hint.ai_addr = NULL;
    hint.ai_next = NULL;
    err = getaddrinfo(argv[1], "ruptime", &hint, &ailist);

    if (0 != err) {
        err_quit("getaddrinfo error: %s", gai_strerror(err));
    }

    for (aip=ailist; NULL!=aip; aip=aip->ai_next) {
        sockfd = connect_retry(aip->ai_family, SOCK_STREAM, 0,
            aip->ai_addr, aip->ai_addrlen);

        if (sockfd < 0) {
            err = errno;
        } else {
            print_uptime(sockfd);
            exit(0);
        }
    }

    err_exit(err, "can't connect to %s", argv[1]);
    return 0;
}
