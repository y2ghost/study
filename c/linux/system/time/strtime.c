#include "common.h"
#include <time.h>
#include <locale.h>

#define SBUF_SIZE 1000

// 给定输入时间、输入格式、输出格式
// 程序自动输出给定格式的时间信息
// 示例: ./strtime "11:14:30pm 1 Feb 2022" "%I:%M:%S%p %d %b %Y" "%F %T"
int main(int argc, char *argv[])
{
    if (argc < 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s input-date-time in-format [out-format]\n", argv[0]);
    }

    if (setlocale(LC_ALL, "") == NULL) {
        err_quit("setlocale");
    }

    struct tm tm;
    memset(&tm, 0, sizeof(struct tm));

    if (strptime(argv[1], argv[2], &tm) == NULL) {
        err_quit("strptime");
    }

    // 用于mktime决定DST是否有效
    tm.tm_isdst = -1;
    printf("calendar time (seconds since Epoch): %ld\n", (long) mktime(&tm));
    char *ofmt = (argc > 3) ? argv[3] : "%H:%M:%S %A, %d %B %Y %Z";
    char sbuf[SBUF_SIZE];

    if (strftime(sbuf, SBUF_SIZE, ofmt, &tm) == 0) {
        err_quit("strftime returned 0");
    }

    printf("strftime() yields: %s\n", sbuf);
    exit(EXIT_SUCCESS);
}
