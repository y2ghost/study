#include "fifo_seqnum.h"
#include <errno.h>

static char clientFifo[CLIENT_FIFO_NAME_LEN] = {0};

static void removeFifo(void)
{
    unlink(clientFifo);
}

int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [seq-len]\n", argv[0]);
    }

    umask(0);
    snprintf(clientFifo, CLIENT_FIFO_NAME_LEN, CLIENT_FIFO_TEMPLATE,
        (long) getpid());

    if (mkfifo(clientFifo, S_IRUSR | S_IWUSR | S_IWGRP) == -1
        && errno != EEXIST) {
        err_sys("mkfifo %s", clientFifo);
    }

    if (atexit(removeFifo) != 0) {
        err_sys("atexit");
    }

    int serverFd = open(SERVER_FIFO, O_WRONLY);
    if (serverFd == -1) {
        err_sys("open %s", SERVER_FIFO);
    }

    struct request req = {0};
    req.pid = getpid();
    req.seqLen = (argc > 1) ? atoi(argv[1]) : 1;

    if (write(serverFd, &req, sizeof(struct request)) !=
        sizeof(struct request)) {
        err_quit("Can't write to server");
    }

    int clientFd = open(clientFifo, O_RDONLY);
    if (clientFd == -1) {
        err_sys("open %s", clientFifo);
    }

    struct response resp = {0};
    if (read(clientFd, &resp, sizeof(struct response))
        != sizeof(struct response)) {
        err_quit("Can't read response from server");
    }

    printf("%d\n", resp.seqNum);
    exit(EXIT_SUCCESS);
}
