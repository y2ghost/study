#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <assert.h>
#include <stdio.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <libgen.h>

int timeout_connect(const char* ip, int port, int time)
{
    struct sockaddr_in address;
    memset(&address, 0x0, sizeof(address));
    address.sin_family = AF_INET;
    inet_pton(AF_INET, ip, &address.sin_addr);
    address.sin_port = htons(port);

    int sockfd = socket(PF_INET, SOCK_STREAM, 0);
    assert(sockfd >= 0);

    struct timeval timeout;
    timeout.tv_sec = time;
    timeout.tv_usec = 0;
    socklen_t len = sizeof(timeout);

    int ret = setsockopt(sockfd, SOL_SOCKET, SO_SNDTIMEO, &timeout, len);
    assert(-1 != ret);

    ret = connect(sockfd, (struct sockaddr*)&address, sizeof(address));
    if (-1 == ret) {
        if (EINPROGRESS == ret) {
            printf("connecting timeout\n");
            return -1;
        }

        printf("error occur when connecting to server\n");
        return -1;
    }

    return sockfd;
}

int main(int argc, char *argv[])
{
    if (argc <= 2) {
        const char *prog_name = basename(argv[0]);
        printf("usage: %s ip_address port_number\n", prog_name);
        return 1;
    }

    const char* ip = argv[1];
    int port = atoi(argv[2]);

    int sockfd = timeout_connect(ip, port, 10);
    if (sockfd < 0) {
        return 1;
    }

    close(sockfd);
    return 0;
}
