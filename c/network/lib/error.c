#include <etcp.h>
#include <stdio.h>
#include <string.h>
#include <stdarg.h>

void error(int status, int err, char *fmt, ...)
{
    va_list ap;

    va_start(ap, fmt);
    vfprintf(stderr, fmt, ap);
    va_end(ap);

    if (0 != err) {
        fprintf(stderr, ": %s (%d)\n", strerror(err), err);
    }

    if (0 != status) {
        exit(status);
    }
}
