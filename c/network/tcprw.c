#include <etcp.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char **argv)
{
    int  sock = -1;
    int rc = 0;
    int len = 0;
    char buf[120] = {'\0'};

    if (3 != argc) {
        fprintf(stderr, "invalid parameters!\n");
        return 1;
    }

    sock = tcp_client(argv[1], argv[2]);
    while (1) {
        if (NULL == fgets(buf, sizeof(buf), stdin)) {
            break;
        }

        len = strlen(buf);
        rc = send(sock, buf, len, 0);
        
        if (rc < 0) {
            error(1, errno, "send failed");
        }

        rc = readline(sock, buf, sizeof(buf));
        if (rc < 0) {
            error(1, errno, "readline failed");
        } else if (rc == 0) {
            error(1, 0, "server terminated\n");
        } else {
            fputs(buf, stdout);
        }
    }

    return 0;
}
