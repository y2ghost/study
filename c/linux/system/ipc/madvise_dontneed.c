#include "common.h"
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>

#define MAP_SIZE 4096
#define WRITE_SIZE 10

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file\n", argv[0]);
    }

    setbuf(stdout, NULL);
    unlink(argv[1]);
    int fd = open(argv[1], O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);

    if (fd == -1) {
        err_sys("open");
    }

    for (int j = 0; j < MAP_SIZE; j++) {
        write(fd, "a", 1);
    }

    if (fsync(fd) == -1) {
        err_sys("fsync");
    }

    close(fd);
    fd = open(argv[1], O_RDWR);

    if (fd == -1) {
        err_sys("open");
    }

    char *addr = mmap(NULL, MAP_SIZE, PROT_READ | PROT_WRITE,
        MAP_PRIVATE, fd, 0);
    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    printf("After mmap:          ");
    write(STDOUT_FILENO, addr, WRITE_SIZE);
    printf("\n");

    for (int j = 0; j < MAP_SIZE; j++) {
        addr[j]++;
    }

    printf("After modification:  ");
    write(STDOUT_FILENO, addr, WRITE_SIZE);
    printf("\n");

    if (madvise(addr, MAP_SIZE, MADV_DONTNEED) == -1) {
        err_sys("madvise");
    }

    printf("After MADV_DONTNEED: ");
    write(STDOUT_FILENO, addr, WRITE_SIZE);
    printf("\n");
    exit(EXIT_SUCCESS);
}
