#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#define SERVERPORT "4950"

int main(int argc, char *argv[])
{
    if (argc != 3) {
        fprintf(stderr,"usage: talker hostname message\n");
        exit(1);
    }

    struct addrinfo *servinfo = NULL;
    struct addrinfo hints;
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_INET6;
    hints.ai_socktype = SOCK_DGRAM;
    int rv = getaddrinfo(argv[1], SERVERPORT, &hints, &servinfo);

    if (0 != rv) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    }

    int sockfd = 0;
    struct addrinfo *p = NULL;

    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((sockfd = socket(p->ai_family, p->ai_socktype,
            p->ai_protocol)) == -1) {
            perror("talker: socket");
            continue;
        }

        break;
    }

    if (p == NULL) {
        fprintf(stderr, "talker: failed to create socket\n");
        return 2;
    }

    int numbytes = sendto(sockfd, argv[2], strlen(argv[2]), 0,
        p->ai_addr, p->ai_addrlen);
    if (-1 == numbytes) {
        perror("talker: sendto");
        exit(1);
    }

    freeaddrinfo(servinfo);
    printf("talker: sent %d bytes to %s\n", numbytes, argv[1]);
    close(sockfd);
    return 0;
}

