#include <etcp.h>
#include <stdio.h>
#include <heartbeat.h>

int main(int argc, char **argv)
{
	fd_set allfd;
	fd_set readfd;
	msg_t msg;
	struct timeval tv;
    int sock = -1;
	int rc = 0;
	int heartbeats = 0;
	int cnt = sizeof(msg);

    if (3 != argc) {
        fprintf(stderr, "invalid parameters\n");
        return 1;
    }

	sock = tcp_client(argv[1], argv[2]);
	FD_ZERO(&allfd);
	FD_SET(sock, &allfd);
	tv.tv_sec = IDLE_TIME;
	tv.tv_usec = 0;

    while (1) {
		readfd = allfd;
		rc = select(sock + 1, &readfd, NULL, NULL, &tv);

		if (rc < 0) {
            error(1, errno, "select failure");
        }

		if (rc == 0) {
            if (++heartbeats > 3) {
                error(1, 0, "connection dead\n");
            }

			error(0, 0, "sending heartbeat #%d\n", heartbeats);
			msg.type = htonl(MSG_HEARTBEAT);
			rc = send(sock, (char*)&msg, sizeof(msg), 0);

			if (rc < 0) {
                error(1, errno, "send failure");
            }

			tv.tv_sec = WAIT_TIME;
			continue;
		}

		if (0 == FD_ISSET(sock, &readfd)) {
            error(1, 0, "select returned invalid socket\n");
        }

		rc = recv(sock, (char*)&msg+sizeof(msg)-cnt, cnt, 0);
		if (rc == 0) {
            error(1, 0, "server terminated\n");
        }

		if (rc < 0) {
            error(1, errno, "recv failure");
        }

		heartbeats = 0;
		tv.tv_sec = IDLE_TIME;
		cnt -= rc;

		if (cnt > 0) {
            continue;
        }

		cnt = sizeof(msg);
	}
}
