#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void do_child(int data_pipe[]) {
    int ch = '\0';
    int read_bytes = 0;

    /* close write pipe */
    close(data_pipe[1]);
    while ((read_bytes = read(data_pipe[0], &ch, 1)) > 0) {
        putchar(ch);
    }

    exit(0);
}

void do_parent(int data_pipe[])
{
    int ch = '\0';
    int exit_code = 0;
    int write_bytes = 0;

    /* close read pipe */
    close(data_pipe[0]);
    while ((ch = getchar()) > 0) {
        write_bytes = write(data_pipe[1], &ch, 1);
        if (-1 == write_bytes) {
            perror("Parent: write");
            exit_code = 1;
        }
    }

    close(data_pipe[1]);
    exit(exit_code);
}

int main(int argc, char* argv[])
{
    int data_pipe[2] = {0, 0};
    int pid = 0;
    int rc = 0;

    rc = pipe(data_pipe);
    if (-1 == rc) {
        perror("pipe");
        exit(1);
    }

    pid = fork();
    switch (pid) {
    case -1:
        perror("fork");
        exit(1);
    case 0:
        do_child(data_pipe);
    default:
        do_parent(data_pipe);
    }

    return 0;
}
