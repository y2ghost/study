#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(void)
{
    time_t t = 0;
    struct tm *tmp = NULL;
    char buf1[16] = {'\0'};
    char buf2[64] = {'\0'};

    time(&t);
    tmp = localtime(&t);
    if (0 == strftime(buf1, 16, "time and date: %r, %a %b %d, %Y", tmp)) {
        printf("buffer length 16 is too small\n");
    } else {
        printf("%s\n", buf1);
    }

    if (0 == strftime(buf2, 64, "time and date: %r, %a %b %d, %Y", tmp)) {
        printf("buffer length 64 is too small\n");
    } else {
        printf("%s\n", buf2);
    }

    return 0;
}
