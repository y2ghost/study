#include "common.h"
#include <sys/socket.h>

#define MAXSLEEP    128

int connect_retry(int domain, int type, int protocol,
    const struct sockaddr *addr, socklen_t alen)
{
    int fd = 0;
    int numsec = 0;

    /*
     * Try to connect with exponential backoff.
     */
    for (numsec=1; numsec<=MAXSLEEP; numsec<<=1) {
        fd = socket(domain, type, protocol);
        if (fd < 0) {
            return -1;
        }

        if (0 == connect(fd,addr,alen)) {
            /*
             * Connection accepted.
             */
            return fd;
        }

        close(fd);
        if (numsec <= MAXSLEEP/2) {
            sleep(numsec);
        }
    }

    return -1;
}
