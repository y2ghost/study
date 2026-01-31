#include "common.h"
#include <pwd.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s username\n", argv[0]);
    }

    size_t bufSize = sysconf(_SC_GETPW_R_SIZE_MAX);
    char *buf = malloc(bufSize);

    if (buf == NULL) {
        err_quit("malloc %d", bufSize);
    }

    struct passwd *result;
    struct passwd pwd;

    int s = getpwnam_r(argv[1], &pwd, buf, bufSize, &result);
    if (s != 0) {
        err_quit("getpwnam_r");
    }

    if (result != NULL) {
        printf("Name: %s\n", pwd.pw_gecos);
    } else {
        printf("Not found\n");
    }

    exit(EXIT_SUCCESS);
}

