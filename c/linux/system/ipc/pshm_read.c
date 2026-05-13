#include "common.h"
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s shm-name\n", argv[0]);
    }

    int fd = shm_open(argv[1], O_RDONLY, 0);
    if (fd == -1) {
        err_sys("shm_open");
    }

    struct stat sb;
    if (fstat(fd, &sb) == -1) {
        err_sys("fstat");
    }

    char *addr = mmap(NULL, sb.st_size, PROT_READ, MAP_SHARED, fd, 0);
    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    if (close(fd) == -1) {
        err_sys("close");
    }

    write(STDOUT_FILENO, addr, sb.st_size);
    write(STDOUT_FILENO, "\n", 1);
    exit(EXIT_SUCCESS);
}
