#include "fifo_seqnum.h"
#include <signal.h>
#include <errno.h>

int main(int argc, char *argv[])
{
    umask(0);
    if (mkfifo(SERVER_FIFO, S_IRUSR | S_IWUSR | S_IWGRP) == -1
        && errno != EEXIST) {
        err_sys("mkfifo %s", SERVER_FIFO);
    }

    int serverFd = open(SERVER_FIFO, O_RDONLY);
    if (serverFd == -1) {
        err_sys("open %s", SERVER_FIFO);
    }

    int dummyFd = open(SERVER_FIFO, O_WRONLY);
    if (dummyFd == -1) {
        err_sys("open %s", SERVER_FIFO);
    }

    if (signal(SIGPIPE, SIG_IGN) == SIG_ERR) {
        err_sys("signal");
    }

    int seqNum = 0;
    struct request req = {0};
    struct response resp = {0};
    char clientFifo[CLIENT_FIFO_NAME_LEN] = {0};

    for (;;) {
        if (read(serverFd, &req, sizeof(struct request))
            != sizeof(struct request)) {
            fprintf(stderr, "Error reading request; discarding\n");
            continue;
        }

        snprintf(clientFifo, CLIENT_FIFO_NAME_LEN, CLIENT_FIFO_TEMPLATE, (long) req.pid);
        int clientFd = open(clientFifo, O_WRONLY);

        if (clientFd == -1) {
            err_msg("open %s", clientFifo);
            continue;
        }

        resp.seqNum = seqNum;
        if (write(clientFd, &resp, sizeof(struct response)) 
                != sizeof(struct response)) {
            fprintf(stderr, "Error writing to FIFO %s\n", clientFifo);
        }

        if (close(clientFd) == -1) {
            err_msg("close");
        }

        seqNum += req.seqLen;
    }
}

