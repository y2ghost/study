#include <time.h>
#include <locale.h>
#include <stdio.h>

int main(int ac, char *av[])
{
    char *prog_name = av[0];
    if (NULL == setlocale(LC_ALL, "")) {
        fprintf(stderr, "%s error to setlocale!\n", prog_name);
        return 1;
    }

    time_t now = time(NULL);
    printf("current time: %s", ctime(&now));

    struct tm *loc = localtime(&now);
    if (NULL == loc) {
        fprintf(stderr, "%s error to localtime!\n", prog_name);
        return 1;
    }

    printf("local time: %s", asctime(loc));
    char sbuf[512] = {'\0'};

    if (0 == strftime(sbuf, sizeof(sbuf), "%A, %d %B %Y, %H:%M:%S %Z", loc)) {
        fprintf(stderr, "%s strftime error!\n", prog_name);
        return 1;
    }

    printf("strftime of local time: %s\n", sbuf);
    return 0;
}
