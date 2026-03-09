#include "common.h"
#include <time.h>
#include <locale.h>

#define BUF_SIZE 200

// 显示本地化时区和时间
int main(int argc, char *argv[])
{
    if (setlocale(LC_ALL, "") == NULL) {
        err_quit("setlocale");
    }

    time_t t = time(NULL);
    printf("ctime() of time() value is:  %s", ctime(&t));
    struct tm *loc = localtime(&t);

    if (loc == NULL) {
        err_quit("localtime");
    }

    printf("asctime() of local time is:  %s", asctime(loc));
    char buf[BUF_SIZE];

    if (strftime(buf, BUF_SIZE, "%A, %d %B %Y, %H:%M:%S %Z", loc) == 0) {
        err_quit("strftime returned 0");
    }

    printf("strftime() of local time is: %s\n", buf);
    exit(EXIT_SUCCESS);
}

