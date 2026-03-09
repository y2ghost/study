#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>

int main(int ac, char **av)
{
    int rc = -1;
    int sock = -1;
    char buf[1] = {'\0'};
    struct sockaddr_in peer;

    peer.sin_family = AF_INET;
    peer.sin_port = htons(7500);
    inet_pton(AF_INET, "127.0.0.1", &peer.sin_addr);
    sock = socket(AF_INET, SOCK_STREAM, 0);

    if (sock < 0) {
        perror("socket call failed");
        return 1;
    }

    rc = connect(sock, (struct sockaddr*)&peer, sizeof(peer));
    if (0 != rc) {
        perror("connect call failed");
        return 1;
    }

    rc = send(sock, "1", 1, 0);
    if (rc <= 0) {
        perror("send call failed");
        return 1;
    }

    rc = recv(sock, buf, sizeof(buf), 0);
    if (rc <= 0) {
        perror("recv call failed");
        return 1;
    } else {
        printf("%c\n", *buf);
    }

    return 0;
}
