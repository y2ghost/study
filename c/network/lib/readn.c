#include <etcp.h>

int readn(int sock, char *bp, size_t len)
{
    int cnt = 0;
    int rc = 0;

    cnt = len;

    while (cnt > 0) {
        rc = recv(sock, bp, cnt, 0);
        if (rc < 0) {
            if (errno == EINTR) {
                continue;
            }

            return -1;
        }

        if (rc == 0) {
            return len - cnt;
        }

        bp += rc;
        cnt -= rc;
    }

    return len;
}
