#include "curr_time.h"
#include <time.h>
#include <stdio.h>

#define BUF_SIZE 1000

char *currTime(const char *format)
{
    static char buf[BUF_SIZE];
    time_t t = time(NULL);
    struct tm *tm = localtime(&t);

    if (tm == NULL) {
        return NULL;
    }

    size_t s = strftime(buf, BUF_SIZE, (format != NULL) ? format : "%c", tm);
    return (s == 0) ? NULL : buf;
}

