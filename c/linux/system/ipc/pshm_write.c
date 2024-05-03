#include "common.h"
#include <fcntl.h>
#include <sys/mman.h>

// 执行示例: ./pshm_write demo_shm 111
int main(int argc, char *argv[])
{
    if (argc != 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s shm-name string\n", argv[0]);
    }

    int fd = shm_open(argv[1], O_RDWR, 0);
    if (fd == -1) {
        err_sys("shm_open");
    }

    size_t len = strlen(argv[2]);
    if (ftruncate(fd, len) == -1) {
        err_sys("ftruncate");
    }

    printf("Resized to %ld bytes\n", (long) len);
    char *addr = mmap(NULL, len, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);

    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    if (close(fd) == -1) {
        err_sys("close");
    }

    printf("copying %ld bytes\n", (long) len);
    memcpy(addr, argv[2], len);
    exit(EXIT_SUCCESS);
}
