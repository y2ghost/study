#include "common.h"
#include <dirent.h>

int
main(int argc, char *argv[])
{
    DIR *dp = NULL;
    struct dirent *dirp = NULL;

    if (2 != argc) {
        err_quit("usage: ls directory_name");
    }

    dp = opendir(argv[1]);
    if (NULL == dp) {
        err_sys("can't open %s", argv[1]);
    }

    while (1) {
        dirp = readdir(dp);
        if (NULL == dirp) {
            break;
        }

        printf("%s\n", dirp->d_name);
    }

    closedir(dp);
    return 0;
}
