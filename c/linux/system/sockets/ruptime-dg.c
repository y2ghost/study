#include "common.h"
#include <netdb.h>
#include <errno.h>
#include <sys/socket.h>

#define BUFLEN  128
#define TIMEOUT 20

void sigalrm(int signo)
{
}

void print_uptime(int sockfd, struct addrinfo *aip)
{
    int n = 0;
    char buf[BUFLEN] = {'\0'};

    buf[0] = 0;
    if (sendto(sockfd, buf, 1, 0, aip->ai_addr, aip->ai_addrlen) < 0) {
        err_sys("sendto error");
    }

    alarm(TIMEOUT);
    n = recvfrom(sockfd, buf, BUFLEN, 0, NULL, NULL);
    
    if (n < 0) {
        if (EINTR != errno) {
            alarm(0);
        }

        err_sys("recv error");
    }
    
    alarm(0);
    write(STDOUT_FILENO, buf, n);
}

int main(int argc, char *argv[])
{
    struct addrinfo *aip = NULL;
    struct addrinfo *ailist = NULL;
    struct addrinfo hint;
    int sockfd = 0;
    int err = 0;
    struct sigaction sa;

    if (2 != argc) {
        err_quit("usage: ruptime hostname");
    }

    sa.sa_handler = sigalrm;
    sa.sa_flags = 0;
    sigemptyset(&sa.sa_mask);

    if (sigaction(SIGALRM, &sa, NULL) < 0) {
        err_sys("sigaction error");
    }

    memset(&hint, 0, sizeof(hint));
    hint.ai_socktype = SOCK_DGRAM;
    hint.ai_canonname = NULL;
    hint.ai_addr = NULL;
    hint.ai_next = NULL;

    err = getaddrinfo(argv[1], "ruptime", &hint, &ailist);
    if (0 != err) {
        err_quit("getaddrinfo error: %s", gai_strerror(err));
    }

    for (aip=ailist; NULL!=aip; aip=aip->ai_next) {
        sockfd = socket(aip->ai_family, SOCK_DGRAM, 0);
        if (sockfd < 0) {
            err = errno;
        } else {
            print_uptime(sockfd, aip);
            exit(0);
        }
    }

    fprintf(stderr, "can't contact %s: %s\n", argv[1], strerror(err));
    return 1;
}
