#include <etcp.h>

int readline(int sock, char *bufptr, size_t len)
{
    char *buf_begin = bufptr;
    static char *bp = NULL;
    static int cnt = 0;
    static char b[1500];
    char c = '\0';

    while (--len > 0) {
        if (--cnt <= 0) {
            cnt = recv(sock, b, sizeof(b), 0);
            if (cnt < 0) {
                if (errno == EINTR) {
                    len++;
                    continue;
                }

                return -1;
            }

            if (cnt == 0) {
                return 0;
            }

            bp = b;
        }

        c = *bp++;
        *bufptr++ = c;

        if (c == '\n') {
            *bufptr = '\0';
            return bufptr - buf_begin;
        }
    }

    errno = EMSGSIZE;
    return -1;
}
