#include "apue.h"

ssize_t readn(int fd, void *ptr, size_t n)
{
    size_t nleft = 0;
    ssize_t nread = 0;

    nleft = n;
    while (nleft > 0) {
        nread = read(fd, ptr, nleft);
        if (nread < 0) {
            if (nleft == n) {
                return -1;
            } else {
                break;
            }
        } else if (0 == nread) {
            break;
        }

        ptr += nread;
        nleft -= nread;
    }

    return n - nleft;
}
