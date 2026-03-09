#include "common.h"
#include <sys/xattr.h>

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file\n", argv[0]);
    }

    char *value = "The past is not dead.";
    if (setxattr(argv[1], "user.x", value, strlen(value), 0) == -1) {
        err_quit("setxattr");
    }

    value = "In fact, it's not even past.";
    if (setxattr(argv[1], "user.y", value, strlen(value), 0) == -1) {
        err_quit("setxattr");
    }

    exit(EXIT_SUCCESS);
}

