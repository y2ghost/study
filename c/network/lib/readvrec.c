#include <etcp.h>

int readvrec(int sock, char *bp, size_t len)
{
    u_int32_t reclen = 0;
    int rc = 0;

    rc = readn(sock, (char*)&reclen, sizeof(reclen));
    if (rc != sizeof(reclen)) {
        return rc < 0 ? -1 : 0;
    }

    reclen = ntohl(reclen);
    if (reclen > len) {
        /*
         *  Not enough room for the record--
         *  discard it and return an error.
         */
        while (reclen > 0) {
            rc = readn(sock, bp, len);
            if (rc != len) {
                return rc < 0 ? -1 : 0;
            }

            reclen -= len;
            if (reclen < len) {
                len = reclen;
            }
        }

        errno = EMSGSIZE;
        return -1;
    }

    /* Retrieve the record itself */
    rc = readn(sock, bp, reclen);
    if (rc != reclen) {
        return rc < 0 ? -1 : 0;
    }

    return rc;
}
