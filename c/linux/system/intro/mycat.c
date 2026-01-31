#include "common.h"

#define BUFFSIZE    4096

int main(void)
{
    int n = 0;
    int rc = 0;
    char buf[BUFFSIZE] = {'\0'};

    while (1) {
        n = read(STDIN_FILENO, buf, BUFFSIZE);
        if (n <= 0) {
            break;
        }

        rc = write(STDOUT_FILENO, buf, n);
        if (rc != n) {
            err_sys("write error");
        }
    }

    if (n < 0) {
        err_sys("read error");
    }

    return 0;
}
