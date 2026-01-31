#include "clififo.h"
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/stat.h>

int main(int argc, char **argv)
{
    /* create server's well-known FIFO; OK if already exists */
    if (mkfifo(SERV_FIFO, FILE_MODE)< 0 && EEXIST!=errno) {
        fprintf(stderr, "can't create %s\n", SERV_FIFO);
        exit(1);
    }

    int readfifo = open(SERV_FIFO, O_RDONLY);
    /* for less call open function */
    open(SERV_FIFO, O_WRONLY);

    while (1) {
        char buff[MAXLINE] = {'\0'};
        ssize_t n = read(readfifo, buff, sizeof(buff));

        if ('\n' == buff[n-1]) {
            n--;
        }

        buff[n] = '\0';
        char *ptr = strchr(buff, ' ');

        if (NULL == ptr) {
            fprintf(stderr, "bogus request: %s\n", buff);
            continue;
        }

        *ptr++ = '\0';
        pid_t pid = atol(buff);
        char fifo_name[MAXLINE] = {'\0'};

        snprintf(fifo_name, sizeof(fifo_name), "/tmp/fifo.%ld", (long)pid);
        int clientfd = open(fifo_name, O_WRONLY);

        if (clientfd < 0) {
            fprintf(stderr, "cannot open: %s\n", fifo_name);
            continue;
        }

        printf("client(%ld) request path: %s\n", (long)pid, ptr);
        int filefd = open(ptr, O_RDONLY);

        if (filefd < 0) {
            snprintf(buff+n, sizeof(buff)-n, ": can't open, %s\n",
                strerror(errno));
            n = strlen(ptr);
            write(clientfd, ptr, n);
            close(clientfd);
            continue;
        }

        /* open succeeded: copy file to FIFO */
        while (1) {
            n = read(filefd, buff, MAXLINE);
            if (n <= 0) {
                break;
            }

            write(clientfd, buff, n);
        }
     
        close(filefd);
        close(clientfd);
    }

    return 0;
}
