#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/wait.h>

#define MAXLINE 4096

static void _client(int readfd, int writefd);
static void _server(int readfd, int writefd);

int main(int ac, char *av[])
{
    int client_pipe[2] = {0};
    int server_pipe[2] = {0};

    pipe(client_pipe);
    pipe(server_pipe);

    /* child process is server side */ 
    pid_t pid = fork();
    if (pid < 0) {
        fprintf(stderr, "%s\n", "failed to build server side");
        exit(1);
    } else if (0 == pid) {
        close(client_pipe[1]);
        close(server_pipe[0]);
        _server(client_pipe[0], server_pipe[1]);
        exit(0);
    }

    close(client_pipe[0]);
    close(server_pipe[1]);
    _client(server_pipe[0], client_pipe[1]);
    waitpid(pid, NULL, 0);
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
