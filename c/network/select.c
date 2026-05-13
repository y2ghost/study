#include <stdio.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>

#define STDIN 0
int main(void)
{
    struct timeval tv;
    tv.tv_sec = 2;
    tv.tv_usec = 500000;

    fd_set readfds;
    FD_ZERO(&readfds);
    FD_SET(STDIN, &readfds);

    // 暂不考虑writefds和exceptfds
    select(STDIN+1, &readfds, NULL, NULL, &tv);
    if (FD_ISSET(STDIN, &readfds)) {
        printf("A key was pressed!\n");
    } else {
        printf("Timed out.\n");
    }

    return 0;
}

