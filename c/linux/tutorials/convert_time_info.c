#define _XOPEN_SOURCE
#include <time.h>
#include <locale.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/* example for test
 * "9:39:46pm 1 Feb 2011" "%I:%M:%S%p %d %b %Y"
 * "9:39:46pm 1 Feb 2011" "%I:%M:%S%p %d %b %Y" "%F %T"
 */
int main(int ac, char *av[])
{
    char *prog_name = av[0];
    if (ac<3 || 0==strcmp("--help", av[1])) {
        fprintf(stderr, "%s input-date-time in-format "
            "[out-format]\n", prog_name);
        return 1;
    }

    if (NULL == setlocale(LC_ALL, "")) {
        fprintf(stderr, "%s set locale error!\n", prog_name);
        return 2;
    }

    struct tm tm;
    memset(&tm, 0x0, sizeof(tm));

    if (NULL == strptime(av[1], av[2], &tm)) {
        fprintf(stderr, "%s strptime failed!\n", prog_name);
        return 3;
    }

    tm.tm_isdst = -1; /* let mktime to decide it */
    time_t utc_time = mktime(&tm);
    printf("UTC: %ld\n", utc_time);

    char *ofmt = "%H:%M:%S %A, %d %B %Y %Z";
    if (ac > 3) {
        ofmt = av[3];
    }

    char sbuf[512] = {'\0'};
    if (0 == strftime(sbuf, sizeof(sbuf), ofmt, &tm)) {
        fprintf(stderr, "%s strftime error!\n", prog_name);
        return 4;
    }

    printf("strftime yields: %s\n", sbuf);
    return 0;
}
