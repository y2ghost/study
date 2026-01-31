#include <sys/stat.h>
#include <sys/uio.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>

int main(int ac, char **av)
{
    int fd = 0;
    struct iovec iov[3];
    struct stat mystat;
    int x = 0;
    char str[100] = {'\0'};
    ssize_t read_bytes = 0;
    ssize_t tot_bytes = 0;

    if (2!=ac || 0==strcmp("--help", av[1])) {
        fprintf(stderr, "%s file\n", av[0]);
        return -1;
    }

    tot_bytes = 0;
    fd = open(av[1], O_RDONLY);
    
    if (-1 == fd) {
        fprintf(stderr, "open error!\n");
        return -1;
    }

    iov[0].iov_base = &mystat;
    iov[0].iov_len = sizeof(mystat);
    tot_bytes += iov[0].iov_len;
    iov[1].iov_base = &x;
    iov[1].iov_len = sizeof(x);
    tot_bytes += iov[1].iov_len;
    iov[2].iov_base = str;
    iov[2].iov_len = sizeof(str);
    tot_bytes += iov[2].iov_len;

    read_bytes = readv(fd, iov, 3);
    if (-1 == read_bytes) {
        fprintf(stderr, "error read file\n");
        return -1;
    }

    if (read_bytes < tot_bytes) {
        printf("read fewer than requested\n");
    }

    printf("total bytes: %ld, read bytes: %ld\n", tot_bytes, read_bytes);
    return 0;
}
