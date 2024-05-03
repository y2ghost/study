#include "net_utils.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

struct addrinfo *host_serv(const char *host, const char *service,
    int family, int socktype)
{
    struct addrinfo hints = {0};
    hints.ai_flags = AI_CANONNAME;
    hints.ai_family = family;
    hints.ai_socktype = socktype;

    struct addrinfo *res = NULL;
    getaddrinfo(host, service, &hints, &res);
    return res;
}

int tcp_connect(const char *host, const char *service)
{
    struct addrinfo *res = NULL;
    struct addrinfo hints = {0};

    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;

    int rc = getaddrinfo(host, service, &hints, &res);
    if (0 != rc) {
        fprintf(stderr, "tcp_connect error for %s, %s: %s",
                 host, service, gai_strerror(rc));
        return -1;
    }

    int sockfd = -1;
    struct addrinfo *tmp_res = NULL;

    for (tmp_res=res; NULL!=tmp_res; tmp_res=tmp_res->ai_next) {
        sockfd = socket(res->ai_family, res->ai_socktype,
            res->ai_protocol);
        if (sockfd < 0) {
            continue;
        }

        rc = connect(sockfd, res->ai_addr, res->ai_addrlen);
        if (0 == rc) {
            break;
        }

        close(sockfd);
    }

    if (NULL == tmp_res) {
        fprintf(stderr, "tcp_connect error for %s, %s",
            host, service);
    }

    freeaddrinfo(res);
    return sockfd;
}

int tcp_listen(const char *host, const char *service, socklen_t *len)
{
    struct addrinfo *res = NULL;
    struct addrinfo hints = {0};

    hints.ai_flags = AI_PASSIVE;
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;

    int rc = getaddrinfo(host, service, &hints, &res);
    if (0 != rc) {
        fprintf(stderr, "tcp_listen error for %s, %s: %s",
                 host, service, gai_strerror(rc));
        return -1;
    }

    int listenfd = -1;
    struct addrinfo *tmp_res = NULL;

    for (tmp_res=res; NULL!=tmp_res; tmp_res=tmp_res->ai_next) {
        const int on = 1;
        listenfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);

        if (listenfd < 0) {
            continue;
        }

        setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on));
        rc = bind(listenfd, res->ai_addr, res->ai_addrlen);

        if (0 == rc) {
            break;
        }

        close(listenfd);
    }

    if (NULL != tmp_res) {
        listen(listenfd, 1024);
        if (NULL != len) {
            *len= res->ai_addrlen;
        }
    } else {
        fprintf(stderr, "tcp_listen error for %s, %s",
            host, service);
    }

    freeaddrinfo(res);
    return listenfd;
}

int udp_client(const char *host, const char *service,
    struct sockaddr **sa, socklen_t *len)
{
    struct addrinfo *res = NULL;
    struct addrinfo hints = {0};

    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_DGRAM;

    int rc = getaddrinfo(host, service, &hints, &res);
    if (0 != rc) {
        fprintf(stderr, "udp_client error for %s, %s: %s",
            host, service, gai_strerror(rc));
        return -1;
    }

    int sockfd = -1;
    struct addrinfo *tmp_res = NULL;

    for (tmp_res=res; NULL!=tmp_res; tmp_res=tmp_res->ai_next) {
        sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
        if (sockfd >= 0) {
            break;
        }
   }

    if (NULL != tmp_res) {
        *sa = malloc(tmp_res->ai_addrlen);
        memcpy(*sa, tmp_res->ai_addr, tmp_res->ai_addrlen);
        *len = tmp_res->ai_addrlen;
    } else {
        fprintf(stderr, "udp_client error for %s, %s", host, service);
    }


    freeaddrinfo(res);
    return sockfd;
}

int udp_connect(const char *host, const char *service)
{
    struct addrinfo *res = NULL;
    struct addrinfo hints = {0};

    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_DGRAM;

    int rc = getaddrinfo(host, service, &hints, &res);
    if (0 != rc) {
        fprintf(stderr, "udp_connect error for %s, %s: %s",
            host, service, gai_strerror(rc));
        return -1;
    }

    int sockfd = -1;
    struct addrinfo *tmp_res = res;

    for (tmp_res=res; NULL!=tmp_res; tmp_res=tmp_res->ai_next) {
        sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
        if (sockfd < 0) {
            continue;
        }

        rc = connect(sockfd, res->ai_addr, res->ai_addrlen);
        if (0 == rc) {
            break;
        }

        close(sockfd);
    }

    if (NULL == tmp_res) {
        fprintf(stderr, "udp_connect error for %s, %s", host, service);
    }

    freeaddrinfo(res);
    return sockfd;
}

int udp_server(const char *host, const char *service, socklen_t *len)
{
    struct addrinfo *res = NULL;
    struct addrinfo hints = {0};

    hints.ai_flags = AI_PASSIVE;
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_DGRAM;

    int rc = getaddrinfo(host, service, &hints, &res);
    if (0 != rc) {
        fprintf(stderr, "udp_server error for %s, %s: %s",
            host, service, gai_strerror(rc));
        return -1;
    }

    int sockfd = -1;
    struct addrinfo *tmp_res = NULL;

    for (tmp_res=res; NULL!=tmp_res; tmp_res=tmp_res->ai_next) {
        sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
        if (sockfd < 0) {
            continue;
        }

        rc = bind(sockfd, res->ai_addr, res->ai_addrlen);
        if (0 == rc) {
            break;
        }

        close(sockfd);
    }

    if (NULL != tmp_res) {
        if (NULL != len) {
            *len = res->ai_addrlen;
        }
    } else {
        fprintf(stderr, "udp_server error for %s, %s", host, service);
    }


    freeaddrinfo(res);
    return sockfd;
}
