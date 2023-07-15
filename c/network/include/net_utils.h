#ifndef NET_UTILS_H
#define NET_UTILS_H

#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>

struct addrinfo *host_serv(const char *host, const char *service,
    int family, int socktype);
int tcp_connect(const char *host, const char *service);
int tcp_listen(const char *host, const char *service, socklen_t *len);
int udp_client(const char *host, const char *service,
    struct sockaddr **sa, socklen_t *len);
int udp_connect(const char *host, const char *service);
int udp_server(const char *host, const char *service, socklen_t *len);

#endif /* NET_UTILS_H */
