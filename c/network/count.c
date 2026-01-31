#include <etcp.h>
#include <stdio.h>

int main(int argc, char **argv)
{
    int serv_sock = -1;
    int peer_sock = -1;
	int rc = 0;
	int len = 0;
	int counter = 1;
	char buf[120] = {'\0'};

    if (2 != argc) {
        fprintf(stderr, "usage: %s port\n", argv[0]);
        return 1;
    }

	serv_sock = tcp_server(NULL, argv[1]);
	peer_sock = accept(serv_sock, NULL, NULL);

    if (peer_sock < 0) {
        error(1, errno, "accept failed");
    }

    while (1) {
        rc = readline(peer_sock, buf, sizeof(buf));
        if (rc <= 0) {
            break;
        }

		sleep(5);
		len = sprintf(buf, "received message %d\n", counter++);
		rc = send(peer_sock, buf, len, 0);

		if (rc < 0) {
            error(1, errno, "send failed");
        }
	}

    return 0;
}
