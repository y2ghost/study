#include "common.h"
#include <errno.h>
#include <stdarg.h>
#include <syslog.h>

extern int  log_to_stderr;

static void log_doit(int, int, int, const char *, va_list ap);

void log_open(const char *ident, int option, int facility)
{
    if (0 == log_to_stderr) {
        openlog(ident, option, facility);
    }
}

void log_ret(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    log_doit(1, errno, LOG_ERR, fmt, ap);
    va_end(ap);
}

void log_sys(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    log_doit(1, errno, LOG_ERR, fmt, ap);
    va_end(ap);
    exit(2);
}

void log_msg(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    log_doit(0, 0, LOG_ERR, fmt, ap);
    va_end(ap);
}

void log_quit(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    log_doit(0, 0, LOG_ERR, fmt, ap);
    va_end(ap);
    exit(2);
}

void log_exit(int error, const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    log_doit(1, error, LOG_ERR, fmt, ap);
    va_end(ap);
    exit(2);
}

static void log_doit(int errnoflag, int error, int priority,
    const char *fmt, va_list ap)
{
    char err[128] = {'\0'};
    char buf[MAXLINE] = {'\0'};
    char * pos = buf;
    int left = MAXLINE;
    int len = vsnprintf(pos, left, fmt, ap);

    if (len >= left) {
        len = left - 1;
    }

    if (0 != errnoflag) {
        snprintf(err, sizeof(err), ": %s", strerror(error));
    }

    pos += len;
    left -= len;
    snprintf(pos, left, "%s\n", err);

    if (0 != log_to_stderr) {
        fflush(stdout);
        fputs(buf, stderr);
        fflush(stderr);
    } else {
        syslog(priority, "%s", buf);
    }
}

