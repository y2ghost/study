#include <etcp.h>

int udp_server(const char *hname, const char *sname)
{
    int sock = 0;
    struct sockaddr_in serv;

    set_address(hname, sname, "udp", &serv);
    sock = socket(AF_INET, SOCK_DGRAM, 0);

    if (sock < 0) {
        error(1, errno, "socket call failed");
    }

    if (0 != bind(sock, (struct sockaddr *) &serv, sizeof(serv))) {
        error(1, errno, "bind failed");
    }

    return sock;
}
