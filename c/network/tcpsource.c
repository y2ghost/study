#include <etcp.h>

int main(int argc, char **argv)
{
    struct sockaddr_in peer;
    char *buf = NULL;
    int sock = -1;
    int c = 0;
    int blks = 5000;
    int sndbufsz = 32 * 1024;
    int sndsz = 1440;

    opterr = 0;
    while (1) {
        c = getopt(argc, argv, "s:b:c:");
        if (-1 == c) {
            break;
        }

        switch (c) {
        case 's':
            sndsz = atoi(optarg);
            break;
        case 'b':
            sndbufsz = atoi(optarg);
            break;
        case 'c':
            blks = atoi(optarg);
            break;
        case '?' :
            error(1, 0, "illegal option: %c\n", c);
        }
    }

    if (argc <= optind) {
        error(1, 0, "missing host name\n");
    }

    buf = malloc(sndsz);
    if (NULL == buf) {
        error(1, 0, "malloc failed\n");
    }

    set_address(argv[optind], "9000", "tcp", &peer);
    sock = socket(AF_INET, SOCK_STREAM, 0);

    if (sock < 0) {
        error(1, errno, "socket call failed");
    }

    if (setsockopt(sock, SOL_SOCKET, SO_SNDBUF,
        (char*)&sndbufsz, sizeof(sndbufsz))) {
        error(1, errno, "setsockopt SO_SNDBUF failed");
    }

    if (connect(sock, (struct sockaddr *)&peer, sizeof(peer))) {
        error(1, errno, "connect failed");
    }

    while(blks-- > 0) {
        send(sock, buf, sndsz, 0);
    }

    return 0;
}
