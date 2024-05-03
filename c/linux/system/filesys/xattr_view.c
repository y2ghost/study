#include "common.h"
#include <sys/xattr.h>

#define XATTR_SIZE 10000

static void usageError(char *progName)
{
    fprintf(stderr, "Usage: %s [-x] file...\n", progName);
    exit(EXIT_FAILURE);
}

int main(int argc, char *argv[])
{
    int opt = 0;
    int hexDisplay = 0;

    while ((opt = getopt(argc, argv, "x")) != -1) {
        switch (opt) {
        case 'x': hexDisplay = 1;       break;
        case '?': usageError(argv[0]);
        }
    }

    if (optind >= argc) {
        usageError(argv[0]);
    }


    for (int j = optind; j < argc; j++) {
        char list[XATTR_SIZE] = {0};
        ssize_t listLen = listxattr(argv[j], list, XATTR_SIZE);

        if (listLen == -1) {
            err_quit("listxattr");
        }

        printf("%s:\n", argv[j]);
        for (int ns = 0; ns < listLen; ns += strlen(&list[ns]) + 1) {
            char value[XATTR_SIZE] = {0};
            printf("        name=%s; ", &list[ns]);
            ssize_t valueLen = getxattr(argv[j], &list[ns], value, XATTR_SIZE);

            if (valueLen == -1) {
                printf("couldn't get value");
            } else if (!hexDisplay) {
                printf("value=%.*s", (int) valueLen, value);
            } else {
                printf("value=");
                for (int k = 0; k < valueLen; k++) {
                    printf("%02x ", (unsigned char) value[k]);
                }
            }

            printf("\n");
        }

        printf("\n");
    }

    exit(EXIT_SUCCESS);
}

