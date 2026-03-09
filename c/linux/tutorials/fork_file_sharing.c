#include <sys/stat.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int main(int ac, char **av)
{
    int fd = 0;
    int flags = 0;
    char template[] = "/tmp/testXXXXXX";

    setbuf(stdout, NULL);
    fd = mkstemp(template);
    long long offset = lseek(fd, 0, SEEK_CUR);
    printf("File offset before fork(): %lld\n", offset);
    flags = fcntl(fd, F_GETFL);
    printf("O_APPEND flag before fork() is: %s\n",
        (flags & O_APPEND) ? "on" : "off");

    switch (fork()) {
    case -1:
        break;
    case 0:
        lseek(fd, 1000, SEEK_SET);
        flags |= O_APPEND;
        fcntl(fd, F_SETFL, flags);
        break;
    default:
        wait(NULL);
        offset = lseek(fd, 0, SEEK_CUR);
        printf("File offset in parent: %lld\n", offset);
        printf("O_APPEND flag in parent is: %s\n",
            (flags & O_APPEND) ? "on" : "off");
    }

    return 0;
}
