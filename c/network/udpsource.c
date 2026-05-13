#include <etcp.h>
#include <stdio.h>

int main(int argc, char **argv)
{
    struct sockaddr_in peer;
    int sock = 0;
    int rc = 0;
    int datagrams = 0;
    int dgramsz = 1440;
    char buf[1440] = {'\0'};

    if (argc < 3) {
        fprintf(stderr, "usage: %s server datagrams dgramsz\n", argv[0]);
        return 1;
    }

    datagrams = atoi(argv[2]);
    if (argc > 3) {
        dgramsz = atoi(argv[3]);
    }

    sock= udp_client(argv[1], "9000", &peer);
    while (datagrams-- > 0) {
        rc = sendto(sock, buf, dgramsz, 0, (struct sockaddr*)&peer, sizeof(peer));
        if (rc <= 0) {
            error(0, errno, "sendto failed");
        }
    }

    sendto(sock, "", 0, 0, (struct sockaddr*)&peer, sizeof(peer));
    return 0;
}
