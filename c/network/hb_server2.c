#include <etcp.h>
#include <stdio.h>
#include <heartbeat.h>

int main(int argc, char **argv)
{
	fd_set allfd;
	fd_set readfd;
	char msg[1024] = {'\0'};
	struct sockaddr_in peer;
	struct timeval tv;
	int sock = -1;
	int sdata = -1;
	int shb = -1;
	int rc = 0;
	int maxfd1 = 0;
	int missed_heartbeats = 0;
	int peerlen = sizeof(peer);
	char hbmsg[1] = {'\0'};

    if (2 != argc) {
        fprintf(stderr, "invalid parameters!\n");
        return 1;
    }

	sock = tcp_server(NULL, argv[1]);
	sdata = accept(sock, (struct sockaddr*)&peer, (socklen_t*)&peerlen);

    if (sdata < 0) {
		error(1, errno, "accept failed");
    }

	rc = readn(sdata, (char *)&peer.sin_port, sizeof(peer.sin_port));
	if (rc < 0) {
        error(1, errno, "error reading port number");
    }

	shb = socket(PF_INET, SOCK_STREAM, 0);
    if (shb < 0) {
		error(1, errno, "shb socket failure");
    }

	rc = connect(shb, (struct sockaddr*)&peer, peerlen);
	if (rc) {
        error(1, errno, "shb connect error");
    }

	tv.tv_sec = IDLE_TIME + WAIT_TIME;
	tv.tv_usec = 0;
	FD_ZERO(&allfd);
	FD_SET(sdata, &allfd);
	FD_SET(shb, &allfd);
	maxfd1 = (sdata > shb ? sdata : shb) + 1;

    while (1) {
		readfd = allfd;
		rc = select(maxfd1, &readfd, NULL, NULL, &tv);

		if (rc < 0) {
            error(1, errno, "select failure");
        }

		if (rc == 0) {
			if (++missed_heartbeats > 3) {
                error(1, 0, "connection dead\n");
            }

			error(0, 0, "missed heartbeat #%d\n", missed_heartbeats);
			tv.tv_sec = WAIT_TIME;
			continue;
		}

		if (0 != FD_ISSET(shb, &readfd)) {
			rc = recv(shb, hbmsg, 1, 0);
			if (rc == 0) {
                error(1, 0, "client terminated\n");
            }

			if (rc < 0) {
                error(1, errno, "shb recv failure");
            }

			rc = send(shb, hbmsg, 1, 0);
			if (rc < 0) {
                error(1, errno, "shb send failure");
            }
		}
		if (0 != FD_ISSET(sdata, &readfd)) {
			rc = recv(sdata, msg, sizeof(msg), 0);
			if (rc == 0) {
                error(1, 0, "client terminated\n");
            }

			if (rc < 0) {
                error(1, errno, "recv failure");
            }

			/* process data */
		}

		missed_heartbeats = 0;
		tv.tv_sec = IDLE_TIME + WAIT_TIME;
	}

    return 0;
}
