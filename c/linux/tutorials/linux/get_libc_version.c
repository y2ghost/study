#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <string.h>
#include <gnu/libc-version.h>

int main(void)
{
    char libc_version[64] = {'\0'};
    int len = confstr(_CS_PATH,
        libc_version, sizeof(libc_version));

    if (0==len && EINVAL==errno) {
        printf("confstr: %d(%s)\n", errno, strerror(errno));
    }

    printf("libc version: %s, len: %d\n", libc_version, len);
    printf("libc version: %s\n", gnu_get_libc_version());
    return 0;
}
