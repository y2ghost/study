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
    char fifo_name[MAXLINE] = {'\0'};
    pid_t pid = getpid();
    snprintf(fifo_name, sizeof(fifo_name), "/tmp/fifo.%ld", (long)pid);

    if (mkfifo(fifo_name, FILE_MODE)<0 && EEXIST!=errno) {
        fprintf(stderr, "can't create %s", fifo_name);
        exit(1);
    }

    char buff[MAXLINE] = {'\0'};
    char *ptr = buff;
    size_t left = sizeof(buff);
    size_t len = snprintf(ptr, left, "%ld ", (long)pid);
    ptr += len;
    left -= len;

    printf("%s", "give me a path: ");
    fgets(ptr, left, stdin);
    len = strlen(buff);

    int serverfd = open(SERV_FIFO, O_WRONLY);
    write(serverfd, buff, len);

    int readfifo = open(fifo_name, O_RDONLY);
    while (1) {
        ssize_t n = read(readfifo, buff, sizeof(buff));
        if (n <= 0) {
            break;
        }

        write(STDOUT_FILENO, buff, n);
    }

    close(readfifo);
    unlink(fifo_name);
    return 0;
}
