#include "util.h"
#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/wait.h>

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
        server(client_pipe[0], server_pipe[1]);
        exit(0);
    }

    close(client_pipe[0]);
    close(server_pipe[1]);
    client(server_pipe[0], client_pipe[1]);
    waitpid(pid, NULL, 0);
    return 0;
}
