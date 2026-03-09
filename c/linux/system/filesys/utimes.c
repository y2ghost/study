#include "common.h"
#include <sys/stat.h>
#include <sys/time.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file\n", argv[0]);
    }

    struct stat sb;
    if (stat(argv[1], &sb) == -1) {
        err_quit("stat");
    }

    struct timeval tv[2];
    tv[0].tv_sec = sb.st_atime;
    tv[0].tv_usec = 223344;
    tv[1].tv_sec = sb.st_atime;
    tv[1].tv_usec = 667788;

    if (utimes(argv[1], tv) == -1) {
        err_quit("utimes");
    }

    exit(EXIT_SUCCESS);
}

