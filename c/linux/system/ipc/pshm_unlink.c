#include "common.h"
#include <fcntl.h>
#include <sys/mman.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s shm-name\n", argv[0]);
    }

    if (shm_unlink(argv[1]) == -1) {
        err_sys("shm_unlink");
    }

    exit(EXIT_SUCCESS);
}
