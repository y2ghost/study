#include "common.h"
#include <sys/time.h>

void sleep_us(unsigned int nusecs)
{
    struct timeval tval = {0, 0};

    tval.tv_sec = nusecs / 1000000;
    tval.tv_usec = nusecs % 1000000;
    select(0, NULL, NULL, NULL, &tval);
}

