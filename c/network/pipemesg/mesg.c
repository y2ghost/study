#include "mesg.h"
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

ssize_t mesg_recv(int fd, struct mymesg *mptr)
{
    ssize_t n = read(fd, mptr, MESGHDRSIZE);
    /* read EOF */
    if (0 == n) {
        return 0;
    }

    if (MESGHDRSIZE != n) {
        fprintf(stderr, "message header: expected %ld, got %ld\n",
            MESGHDRSIZE, (long)n);
        exit(1);
    }

    long len = mptr->len;
    if (len > 0) {
        n = read(fd, mptr->data, len);
        if (len != n) {
            fprintf(stderr, "message data: expected %ld, got %ld\n",
                len, n);
            exit(1);
        }
    }

    return len;
}

ssize_t mesg_send(int fd, struct mymesg *mptr)
{
    return write(fd, mptr, MESGHDRSIZE+mptr->len);
}
