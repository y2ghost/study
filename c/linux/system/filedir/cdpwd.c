#include "common.h"

int main(void)
{
    size_t size = 0;
    char *ptr = NULL;

    if (chdir("/opt") < 0) {
        err_sys("chdir failed");
    }

    ptr = path_alloc(&size);
    if (NULL == getcwd(ptr, size)) {
        err_sys("getcwd failed");
    }

    printf("cwd = %s\n", ptr);
    return 0;
}
