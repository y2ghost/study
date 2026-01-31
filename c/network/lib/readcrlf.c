#include <etcp.h>

int readcrlf(int sock, char *buf, size_t len)
{
    int rc = 0;
    char c = '\0';
    char lastc = 0;
    char *buf_begin = buf;

    while (len > 0) {
        rc = recv(sock, &c, 1, 0);
        if (1 != c) {
            /*
             *  If we were interrupted, keep going,
             *  otherwise, return EOF or the error.
             */
            if (rc<0 && errno==EINTR) {
                continue;
            }

            return rc;
        }

        if (c == '\n') {
            if (lastc == '\r') {
                buf--;
            }

            *buf = '\0';
            return buf - buf_begin;
        }

        *buf++ = c;
        lastc = c;
        len--;
    }

    errno = EMSGSIZE;
    return -1;
}
