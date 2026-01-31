#include "util.h"
#include "mesg.h"
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

void client(int readfd, int writefd)
{
    struct mymesg mesg = {0};
    printf("%s: ", "give a pathname");
    fgets(mesg.data, MAXMESGDATA, stdin);

    size_t len = strlen(mesg.data);
    if ('\n' == mesg.data[len-1]) {
        len--;
    }

    mesg.len = len;
    mesg.type = 1;
    mesg_send(writefd, &mesg);

    /* read from IPC, write to standard output */
    while (1) {
        ssize_t n = mesg_recv(readfd, &mesg);
        if (n <= 0) {
            break;
        }

        write(STDOUT_FILENO, mesg.data, n);
    }
}

void server(int readfd, int writefd)
{
    /* read pathname from IPC channel */
    struct mymesg mesg = {0, 1};
    ssize_t n = mesg_recv(readfd, &mesg);

    if (n <= 0) {
        fprintf(stderr, "%s\n", "pathname missing");
        exit(1);
    }

    mesg.data[n] = '\0';
    FILE *fp = fopen(mesg.data, "r");
    if (NULL == fp) {
        snprintf(mesg.data+n, sizeof(mesg.data)-n,
            ": can't open, %s\n", strerror(errno));
        mesg.len = strlen(mesg.data);
        mesg_send(writefd, &mesg);
    } else {
        /* fopen succeeded: copy file to IPC channel */
        while (1) {
            char *line = fgets(mesg.data, sizeof(mesg.data), fp);
            if (NULL == line) {
                break;
            }

            mesg.len = strlen(mesg.data);
            mesg_send(writefd, &mesg);
        }

        fclose(fp);
    }

    /* send a 0-length message to signify the end */
    mesg.len = 0;
    mesg_send(writefd, &mesg);
}
