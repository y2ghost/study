#include <etcp.h>

int udp_client(const char *hname, const char *sname, struct sockaddr_in *sap)
{
    int sock = 0;

    set_address(hname, sname, "udp", sap);
    sock = socket(AF_INET, SOCK_DGRAM, 0);

    if (sock < 0) {
        error(1, errno, "socket call failed");
    }

    return sock;
}
