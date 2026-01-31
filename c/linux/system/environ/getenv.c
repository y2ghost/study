#include "common.h"

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s environ-var\n", argv[0]);
    }

    char *val = getenv(argv[1]);
    printf("%s\n", (val != NULL) ? val : "变量不存在");
    exit(EXIT_SUCCESS);
}
