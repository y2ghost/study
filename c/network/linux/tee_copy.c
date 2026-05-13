#define _GNU_SOURCE
#include <assert.h>
#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    if (argc != 2) {
        printf("usage: %s <file>\n", argv[0]);
        return 1;
    }

    int filefd = open(argv[1], O_CREAT | O_WRONLY | O_TRUNC, 0666);
    assert(filefd > 0);

    int pipefd_stdout[2] = {0};
    int ret = pipe(pipefd_stdout);
    assert(-1 != ret);

    int pipefd_file[2] = {0};
    ret = pipe(pipefd_file);
    assert(-1 != ret);

    ret = splice(STDIN_FILENO, NULL, pipefd_stdout[1], NULL, 32768,
        SPLICE_F_MORE | SPLICE_F_MOVE);
    assert(-1 != ret);

    ret = tee(pipefd_stdout[0], pipefd_file[1], 32768, SPLICE_F_NONBLOCK); 
    assert(-1 != ret);

    ret = splice(pipefd_file[0], NULL, filefd, NULL, 32768, SPLICE_F_MORE | SPLICE_F_MOVE);
    assert(-1 != ret);

    ret = splice(pipefd_stdout[0], NULL, STDOUT_FILENO, NULL, 32768,
        SPLICE_F_MORE | SPLICE_F_MOVE);
    assert(-1 != ret);

    close(filefd);
    close(pipefd_stdout[0]);
    close(pipefd_stdout[1]);
    close(pipefd_file[0]);
    close(pipefd_file[1]);
    return 0;
}
