#include <stdio.h>

void show_bytes(void *bytes, int len)
{
    int i = 0;

    for (i=0; i<len; i++) {
        printf(" %.2x", ((unsigned char *)bytes)[i]);
    }

    printf("\n");
}
