#include <etcp.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char **argv)
{
    int sock = 0;
	int n = 0;
	struct {
		u_int32_t reclen;
		char buf[128];
	} packet;

    if (argc != 3) {
        fprintf(stderr, "invalid parameters!\n");
        return 1;
    }

	sock = tcp_client(argv[ 1 ], argv[ 2 ]);
    while (1) {
        if (NULL == fgets(packet.buf, sizeof(packet.buf), stdin)) {
            break;
        }

        n = strlen(packet.buf);
        packet.reclen = htonl(n);
        
        if (send(sock, (char*)&packet, n+sizeof(packet.reclen), 0) < 0) {
            error(1, errno, "send failure");
        }
    }

    return 0;
}
