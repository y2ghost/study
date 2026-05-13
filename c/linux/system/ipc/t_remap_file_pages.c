#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/mman.h>

int main(int argc, char *argv[])
{
    int fd = open("/tmp/tfile", O_CREAT | O_TRUNC | O_RDWR, S_IRUSR | S_IWUSR);
    if (fd == -1) {
        err_sys("open");
    }

    long pageSize = sysconf(_SC_PAGESIZE);
    if (pageSize == -1) {
        err_quit("Couldn't determine page size");
    }

    for (char ch = 'a'; ch < 'd'; ch++) {
        for (int j = 0; j < pageSize; j++) {
            write(fd, &ch, 1);
        }
    }

    system("od -a /tmp/tfile");
    char *addr = mmap(NULL, 3 * pageSize, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);

    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    printf("Mapped at address %p\n", addr);
    if (remap_file_pages(addr, pageSize, 0, 2, 0) == -1) {
        err_sys("remap_file_pages");
    }

    if (remap_file_pages(addr + 2 * pageSize, pageSize, 0, 0, 0) == -1) {
        err_sys("remap_file_pages");
    }

    for (int j = 0; j < 0x100; j++) {
        *(addr + j) = '0';
    }

    for (int j = 0; j < 0x100; j++) {
        *(addr + 2 * pageSize + j) = '2';
    }

    system("od -a /tmp/tfile");
    exit(EXIT_SUCCESS);
}
