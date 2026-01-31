#include "common.h"
#include <sys/mount.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s mount-point\n", argv[0]);
    }

    if (umount(argv[1]) == -1) {
        err_quit("umount");
    }

    exit(EXIT_SUCCESS);
}

