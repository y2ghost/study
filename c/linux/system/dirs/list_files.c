#include "common.h"
#include <dirent.h>
#include <errno.h>

static void listFiles(const char *dirpath)
{
    int isCurrent = strcmp(dirpath, ".") == 0;
    DIR *dirp = opendir(dirpath);

    if (dirp  == NULL) {
        err_msg("opendir failed on '%s'", dirpath);
        return;
    }

    for (;;) {
        errno = 0;
        struct dirent *dp = readdir(dirp);
        if (dp == NULL) {
            break;
        }

        if (strcmp(dp->d_name, ".") == 0 || strcmp(dp->d_name, "..") == 0) {
            continue;
        }

        if (!isCurrent) {
            printf("%s/", dirpath);
        }

        printf("%s\n", dp->d_name);
    }

    if (errno != 0) {
        err_quit("readdir");
    }

    if (closedir(dirp) == -1) {
        err_quit("closedir");
    }
}

int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [dir-path...]\n", argv[0]);
    }

    if (argc == 1) {
        listFiles(".");
    } else {
        for (argv++; *argv; argv++) {
            listFiles(*argv);
        }
    }

    exit(EXIT_SUCCESS);
}

