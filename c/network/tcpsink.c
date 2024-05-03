#include <etcp.h>

static void server(int sock, int rcvbufsz)
{
    char *buf = NULL;
    int rc = 0;
    int bytes = 0;

    buf = malloc(rcvbufsz);
    if (NULL == buf) {
        error(1, 0, "malloc failed\n");
    }

    while (1) {
        rc = recv(sock, buf, rcvbufsz, 0);
        if (rc <= 0) {
            break;
        }

        bytes += rc;
    }

    error(0, 0, "%d bytes received\n", bytes);
}

int main(int argc, char **argv)
{
    struct sockaddr_in serv;
    struct sockaddr_in peer;
    int peerlen = 0;
    int serv_sock = -1;
    int peer_sock = -1;
    int c = 0;
    int rcvbufsz = 32 * 1024;
    const int on = 1;

    opterr = 0;
    while (1) {
        c = getopt(argc, argv, "b:");
        if (-1 == c) {
            break;
        }

        switch (c) {
        case 'b':
            rcvbufsz = atoi(optarg);
            break;
        case '?':
            error(1, 0, "illegal option: %c\n", c);
        }
    }

    set_address(NULL, "9000", "tcp", &serv);
    serv_sock  = socket(AF_INET, SOCK_STREAM, 0);

    if (serv_sock < 0) {
        error(1, errno, "socket call failed");
    }

    if (setsockopt(serv_sock, SOL_SOCKET, SO_REUSEADDR,
        (char*)&on, sizeof(on))) {
        error(1, errno, "setsockopt SO_REUSEADDR failed");
    }

    if (setsockopt(serv_sock, SOL_SOCKET, SO_RCVBUF,
        (char *)&rcvbufsz, sizeof(rcvbufsz))) {
        error(1, errno, "setsockopt SO_RCVBUF failed");
    }

    if (bind(serv_sock, (struct sockaddr*) &serv, sizeof(serv))) {
        error(1, errno, "bind failed");
    }

    listen(serv_sock, 5);

    do {
        peerlen = sizeof(peer);
        peer_sock = accept(serv_sock, (struct sockaddr*)&peer, (socklen_t*)&peerlen);

        if (peer_sock < 0) {
            error(1, errno, "accept failed");
        }

        server(peer_sock, rcvbufsz);
        close(peer_sock);
    } while (0);

    return 0;
}
