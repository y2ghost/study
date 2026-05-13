#include "common.h"

char *getpass(const char *);

int main(void)
{
    char *ptr = NULL;

    ptr = getpass("Enter password:");
    if (NULL == ptr) {
        err_sys("getpass error");
    }

    printf("password: %s\n", ptr);
    while (0 != *ptr) {
        *ptr++ = 0;
    }

    return 0;
}
