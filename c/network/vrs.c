#include <etcp.h>
#include <stdio.h>

int main(int argc, char **argv)
{
    struct sockaddr_in peer;
    int serv_sock = 0;
    int peer_sock = 0;
    int peerlen = sizeof(peer);
    int n = 0;
    char buf[10] = {'\0'};

    if (argc == 2) {
        serv_sock = tcp_server(NULL, argv[1]);
    } else if (argc == 3) {
        serv_sock = tcp_server(argv[1], argv[2]);
    } else {
        fprintf(stderr, "invalid parameters!\n");
        return 1;
    }

    peer_sock = accept(serv_sock, (struct sockaddr *)&peer, (socklen_t*)&peerlen);
    if (peer_sock < 0) {
        error(1, errno, "accept failed");
    }

    while (1) {
        n = readvrec(peer_sock, buf, sizeof(buf));
        if (n < 0) {
            error(0, errno, "readvrec returned error");
        } else if (n == 0) {
            error(1, 0, "client disconnected\n");
        } else {
            write(1, buf, n);
        }
    }

    return 0;
}
