#include <etcp.h>

int main(int argc, char **argv)
{
    int sock = 0;
    int rc = 0;
    int datagrams = 0;
    int rcvbufsz = 100 * 1440;
    char buf[1440] = {'\0'};

    sock = udp_server(NULL, "9000");
    setsockopt(sock, SOL_SOCKET, SO_RCVBUF, (char*)&rcvbufsz, sizeof(int));

    while (1) {
        rc = recv(sock, buf, sizeof(buf), 0);
        if (rc <= 0) {
            break;
        }

        datagrams++;
    }

    error(0, 0, "%d datagrams received\n", datagrams);
    return 0;
}
