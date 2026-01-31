#include "common.h"
#include <locale.h>
#include <time.h>
#include <sys/time.h>

#define SECONDS_IN_TROPICAL_YEAR (365.24219 * 24 * 60 * 60)

// 获取当前时间，转换为多种格式示例
int main(int argc, char *argv[])
{
    time_t t = time(NULL);
    printf("Seconds since the Epoch (1 Jan 1970): %ld", (long) t);
    printf(" (about %6.3f years)\n", t / SECONDS_IN_TROPICAL_YEAR);

    struct timeval tv;
    if (gettimeofday(&tv, NULL) == -1) {
        err_quit("gettimeofday");
    }

    printf("  gettimeofday() returned %ld secs, %ld microsecs\n",
            (long) tv.tv_sec, (long) tv.tv_usec);
    struct tm *gmp = gmtime(&t);

    if (gmp == NULL) {
        err_quit("gmtime");
    }

    // gmp可能被asctime gmtime修改，保存其副本
    struct tm gm = *gmp;
    printf("Broken down by gmtime():\n");
    printf("  year=%d mon=%d mday=%d hour=%d min=%d sec=%d ", gm.tm_year,
            gm.tm_mon, gm.tm_mday, gm.tm_hour, gm.tm_min, gm.tm_sec);
    printf("wday=%d yday=%d isdst=%d\n", gm.tm_wday, gm.tm_yday, gm.tm_isdst);
    struct tm *locp = localtime(&t);

    if (locp == NULL) {
        err_quit("localtime");
    }

    struct tm loc = *locp;
    printf("Broken down by localtime():\n");
    printf("  year=%d mon=%d mday=%d hour=%d min=%d sec=%d ",
            loc.tm_year, loc.tm_mon, loc.tm_mday,
            loc.tm_hour, loc.tm_min, loc.tm_sec);
    printf("wday=%d yday=%d isdst=%d\n\n",
            loc.tm_wday, loc.tm_yday, loc.tm_isdst);
    printf("asctime() formats the gmtime() value as: %s", asctime(&gm));
    printf("ctime() formats the time() value as:     %s", ctime(&t));
    printf("mktime() of gmtime() value:    %ld secs\n", (long) mktime(&gm));
    printf("mktime() of localtime() value: %ld secs\n", (long) mktime(&loc));
    exit(EXIT_SUCCESS);
}

