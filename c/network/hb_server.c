#include <etcp.h>
#include <stdio.h>
#include <heartbeat.h>

int main(int argc, char **argv)
{
    fd_set allfd;
    fd_set readfd;
    msg_t msg;
    struct timeval tv;
    int serv_sock = -1;
    int peer_sock = -1;
    int rc = 0;
    int missed_heartbeats = 0;
    int cnt = sizeof(msg);

    if (2 != argc) {
        fprintf(stderr, "invalid parameters!\n");
        return 1;
    }

    serv_sock = tcp_server(NULL, argv[1]);
    peer_sock = accept(serv_sock, NULL, NULL);

    if (peer_sock < 0) {
        error(1, errno, "accept failed");
    }

    tv.tv_sec = IDLE_TIME + WAIT_TIME;
    tv.tv_usec = 0;
    FD_ZERO(&allfd);
    FD_SET(peer_sock, &allfd);

    while (1) {
        readfd = allfd;
        rc = select(peer_sock + 1, &readfd, NULL, NULL, &tv);
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

        if (0 == FD_ISSET(peer_sock, &readfd)) {
            error(1, 0, "select returned invalid socket\n");
        }

        rc = recv(peer_sock, (char*)&msg+sizeof(msg)-cnt, cnt, 0);
        if (rc == 0) {
            error(1, 0, "client terminated\n");
        }

        if (rc < 0) {
            error(1, errno, "recv failure");
        }

        missed_heartbeats = 0;
        tv.tv_sec = IDLE_TIME + WAIT_TIME;

        cnt -= rc;
        if (cnt > 0) {
            continue;
        }

        cnt = sizeof(msg);
        switch (ntohl(msg.type)) {
        case MSG_TYPE1:
            /* process type1 message */
            break;
        case MSG_TYPE2:
            /* process type2 message */
            break;

        case MSG_HEARTBEAT:
            rc = send(peer_sock, (char*)&msg, sizeof(msg), 0);
            if (rc < 0) {
                error(1, errno, "send failure");
            }

            break;
        default :
            error(1, 0, "unknown message type (%d)\n", ntohl(msg.type));
        }
    }

    return 0;
}
