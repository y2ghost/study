#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/mman.h>

static void usageError(const char *progName)
{
    fprintf(stderr, "Usage: %s [-cx] shm-name size [octal-perms]\n", progName);
    fprintf(stderr, "    -c   Create shared memory (O_CREAT)\n");
    fprintf(stderr, "    -x   Create exclusively (O_EXCL)\n");
    exit(EXIT_FAILURE);
}

/*
 * 操作示例
 * ./pshm_create -c /demo_shm 10000
 * ls -l /dev/shm
 */
int main(int argc, char *argv[])
{
    int opt = 0;
    int flags = O_RDWR;

    while ((opt = getopt(argc, argv, "cx")) != -1) {
        switch (opt) {
        case 'c':   flags |= O_CREAT;           break;
        case 'x':   flags |= O_EXCL;            break;
        default:    usageError(argv[0]);
        }
    }

    if (optind + 1 >= argc) {
        usageError(argv[0]);
    }

    size_t size = atol(argv[optind + 1]);
    mode_t perms = (argc <= optind + 2) ? (S_IRUSR | S_IWUSR) :
        atol(argv[optind + 2]);
    int fd = shm_open(argv[optind], flags, perms);

    if (fd == -1) {
        err_sys("shm_open");
    }

    if (ftruncate(fd, size) == -1) {
        err_sys("ftruncate");
    }


    void *addr = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    exit(EXIT_SUCCESS);
}

