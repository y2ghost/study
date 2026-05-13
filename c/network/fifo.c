#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>

#define MAXLINE 4096
#define CLIENT_FIFO "/tmp/client"
#define SERVER_FIFO "/tmp/server"

static void _client(int readfd, int writefd);
static void _server(int readfd, int writefd);

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
        _server(readfd, writefd);
        exit(0);
    }

    /* must open CLIENT_FIFO O_WRONLY for child process
     * or be blocked by child process */
    writefd = open(CLIENT_FIFO, O_WRONLY, 0);
    readfd = open(SERVER_FIFO, O_RDONLY, 0);
    _client(readfd, writefd);
    waitpid(pid, NULL, 0);
    close(readfd);
    close(writefd);
    unlink(CLIENT_FIFO);
    unlink(SERVER_FIFO);
    return 0;
}

static void _client(int readfd, int writefd)
{
    char buff[MAXLINE] = {'\0'};
    size_t len = 0;

    fprintf(stdout, "%s", "give me a file path: ");
    fgets(buff, sizeof(buff), stdin);
    len = strlen(buff);

    if ('\n' == buff[len-1]) {
        len--;
    }

    write(writefd, buff, len);
    ssize_t rbytes = 0;

    while (1) {
        rbytes = read(readfd, buff, sizeof(buff));
        if (rbytes <= 0) {
            break;
        }

        write(STDOUT_FILENO, buff, rbytes);
    }
}

static void _server(int readfd, int writefd)
{
    char buff[MAXLINE+1] = {'\0'};
    ssize_t n = read(readfd, buff, sizeof(buff)-1);

    if (n <= 0) {
        fprintf(stderr, "%s\n", "get the file path failed");
        exit(1);
    }

    int fd = open(buff, O_RDONLY);
    if (fd < 0) {
        n = snprintf(buff, sizeof(buff),
            "can't open the file, %s\n", strerror(errno));
        write(writefd, buff, n);
        exit(1);
    }

    while (1) {
        n = read(fd, buff, sizeof(buff));
        if (n <= 0) {
            break;
        }

        write(writefd, buff, n);
    }

    close(fd);
}
