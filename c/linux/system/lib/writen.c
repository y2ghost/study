#include "common.h"

ssize_t writen(int fd, const void *ptr, size_t n)
{
    size_t nleft = n;
    while (nleft > 0) {
        ssize_t nwritten = write(fd, ptr, nleft);
        if (nwritten < 0) {
            if (nleft == n) {
                return -1;
            } else {
                break;
            }
        } else if (0 == nwritten) {
            break;
        }

        ptr += nwritten;
        nleft -= nwritten;
    }

    return n - nleft;
}

