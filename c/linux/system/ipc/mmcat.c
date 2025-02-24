#include "common.h"
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file\n", argv[0]);
    }

    int fd = open(argv[1], O_RDONLY);
    if (fd == -1) {
        err_sys("open");
    }

    struct stat sb;
    if (fstat(fd, &sb) == -1) {
        err_sys("fstat");
    }

    if (sb.st_size == 0) {
        exit(EXIT_SUCCESS);
    }

    char *addr = mmap(NULL, sb.st_size, PROT_READ, MAP_PRIVATE, fd, 0);
    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    if (write(STDOUT_FILENO, addr, sb.st_size) != sb.st_size) {
        err_quit("partial/failed write");
    }

    exit(EXIT_SUCCESS);
}
