#include <locale.h>
#include <time.h>
#include <sys/time.h>
#include <stdio.h>
#include <stdlib.h>

static void _print_tm_info(const struct tm *tmp)
{
    printf("year=%d mon=%d mday=%d hour=%d min=%d sec=%d ",
        tmp->tm_year, tmp->tm_mon, tmp->tm_mday,
        tmp->tm_hour, tmp->tm_min, tmp->tm_sec);
    printf("wday=%d yday=%d isdst=%d\n\n",
        tmp->tm_wday, tmp->tm_yday, tmp->tm_isdst);
}

static char *curr_time(const struct tm *tm)
{
    static char time_str[512] = {'\0'};
    size_t len = strftime(time_str, sizeof(time_str), "%X %x", tm);

    char *str = NULL;
    if (0 != len) {
        str = time_str;
    }

    return str;
}

int main(int ac, char *av[])
{
    struct timeval tv = {0};
    if (0== gettimeofday(&tv, NULL)) {
        printf("gettimeofday returend %ld secs, %ld microsesc\n",
            (long)tv.tv_sec, (long)tv.tv_usec);
    }

    time_t now = time(NULL);
    struct tm *gmp = gmtime(&now);

    if (NULL == gmp) {
        return 1;
    }

    struct tm tm_gm = *gmp;
    _print_tm_info(&tm_gm);

    struct tm *locp = localtime(&now);
    if (NULL == locp) {
        return 2;
    }

    struct tm tm_loc = *locp;
    _print_tm_info(&tm_loc);

    setlocale(LC_ALL, "");
    printf("asctime formats the gmtime value as: %s", asctime(&tm_gm));
    printf("ctime formats the time value as: %s", ctime(&now));
    printf("mktime of gmtime value: %ld\n", mktime(&tm_gm));
    printf("mktime of localtime value: %ld\n", mktime(&tm_loc));
    printf("curr_time of gmtime value: %s\n", curr_time(&tm_gm));
    printf("curr_time of localtime value: %s\n", curr_time(&tm_loc));
    return 0;
}
