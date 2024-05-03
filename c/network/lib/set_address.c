#include <etcp.h>
#include <netdb.h>
#include <string.h>
#include <arpa/inet.h>
#include <stdio.h>

void set_address(const char *hname, const char *sname, const char *protocol,
    struct sockaddr_in *sap)
{
    int socktype = 0;
    if (0 == strcmp("tcp", protocol)) {
        socktype = SOCK_STREAM;
    } else if (0 == strcmp("udp", protocol)) {
        socktype = SOCK_DGRAM;
    } else {
        socktype = 0;
    }

    struct addrinfo hints;
    struct addrinfo *ai = NULL;
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_INET;
    hints.ai_socktype = socktype;
    hints.ai_flags = AI_PASSIVE;
    int rv = getaddrinfo(hname, sname, &hints, &ai);

    if (0 != rv) {
        fprintf(stderr, "selectserver: %s\n", gai_strerror(rv));
        exit(1);
    }

    struct sockaddr_in* sa = (struct sockaddr_in*)ai->ai_addr;
    memcpy(sap, &(sa->sin_addr), sizeof(*sap));
}
