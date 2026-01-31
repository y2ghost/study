#include <etcp.h>

int tcp_client(const char *hname, const char *sname)
{
    int sock;
    struct sockaddr_in peer;

    set_address(hname, sname, "tcp", &peer);
    sock = socket(AF_INET, SOCK_STREAM, 0);

    if (sock < 0) {
        error(1, errno, "socket call failed");
    }

    if (connect(sock , (struct sockaddr *)&peer, sizeof(peer))) {
        error(1, errno, "connect failed");
    }

    return sock;
}
