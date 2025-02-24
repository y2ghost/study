#include "util.h"
#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>

#define CLIENT_FIFO "/tmp/client"
#define SERVER_FIFO "/tmp/server"

int main(int ac, char *av[])
{
    if (mkfifo(CLIENT_FIFO, 0755)<0 && EEXIST!=errno) {
        fprintf(stderr, "can't create %s, %s",
            CLIENT_FIFO, strerror(errno));
        exit(1);
    }

    if (mkfifo(SERVER_FIFO, 0755)<0 && EEXIST!=errno) {
        unlink(CLIENT_FIFO);
        fprintf(stderr, "can't create %s, %s",
            SERVER_FIFO, strerror(errno));
        exit(1);
    }

    /* child process is server side */ 
    int readfd = 0;
    int writefd = 0;
    pid_t pid = fork();

    if (pid < 0) {
        unlink(CLIENT_FIFO);
        unlink(SERVER_FIFO);
        fprintf(stderr, "%s\n", "failed to build server side");
        exit(1);
    } else if (0 == pid) {
        readfd = open(CLIENT_FIFO, O_RDONLY, 0);
        writefd = open(SERVER_FIFO, O_WRONLY, 0);
        server(readfd, writefd);
        exit(0);
    }

    /* must open CLIENT_FIFO O_WRONLY for child process
     * or be blocked by child process */
    writefd = open(CLIENT_FIFO, O_WRONLY, 0);
    readfd = open(SERVER_FIFO, O_RDONLY, 0);
    client(readfd, writefd);
    waitpid(pid, NULL, 0);
    close(readfd);
    close(writefd);
    unlink(CLIENT_FIFO);
    unlink(SERVER_FIFO);
    return 0;
}
