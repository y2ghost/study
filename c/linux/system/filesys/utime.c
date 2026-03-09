#include "common.h"
#include <sys/stat.h>
#include <utime.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file\n", argv[0]);
    }

    char *pathname = argv[1];
    struct stat sb;
    if (stat(pathname, &sb) == -1) {
        err_quit("stat");
    }

    struct utimbuf utb;
    utb.actime = sb.st_atime;
    utb.modtime = sb.st_atime;

    if (utime(pathname, &utb) == -1) {
        err_quit("utime");
    }

    exit(EXIT_SUCCESS);
}
