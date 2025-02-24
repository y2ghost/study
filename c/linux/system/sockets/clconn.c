#include "common.h"
#include <sys/socket.h>

#define MAXSLEEP    128

int connect_retry(int sockfd, const struct sockaddr *addr, socklen_t alen)
{
    int numsec = 0;

    /*
     * Try to connect with exponential backoff.
     */
    for (numsec=1; numsec<=MAXSLEEP; numsec<<=1) {
        if (0 == connect(sockfd, addr, alen)) {
            /*
             * Connection accepted.
             */
            return 0;
        }

        /*
         * Delay before trying again.
         */
        if (numsec <= MAXSLEEP/2) {
            sleep(numsec);
        }
    }

    return -1;
}
