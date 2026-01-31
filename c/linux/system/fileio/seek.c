#include "common.h"

int main(void)
{
    if (-1 == lseek(STDIN_FILENO, 0, SEEK_CUR)) {
        printf("cannot seek\n");
    } else {
        printf("seek OK\n");
    }

    return 0;
}
