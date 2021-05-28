#include "apue.h"
#include <errno.h>
#include <stdarg.h>
#include <syslog.h>

/*
 * Caller must define and set this: nonzero if
 * interactive, zero if daemon
 */
extern int  log_to_stderr;

static void log_doit(int, int, int, const char *, va_list ap);

/*
 * Initialize syslog(), if running as daemon.
 */
void log_open(const char *ident, int option, int facility)
{
    if (0 == log_to_stderr) {
        openlog(ident, option, facility);
    }
}

/*
 * Nonfatal error related to a system call.
 * Print a message with the system's errno value and return.
 */
void log_ret(const char *fmt, ...)
{
    va_list ap;

    va_start(ap, fmt);
    log_doit(1, errno, LOG_ERR, fmt, ap);
    va_end(ap);
}

/*
 * Fatal error related to a system call.
 * Print a message and terminate.
 */
void log_sys(const char *fmt, ...)
{
    va_list ap;

    va_start(ap, fmt);
    log_doit(1, errno, LOG_ERR, fmt, ap);
    va_end(ap);
    exit(2);
}

/*
 * Nonfatal error unrelated to a system call.
 * Print a message and return.
 */
void log_msg(const char *fmt, ...)
{
    va_list ap;

    va_start(ap, fmt);
    log_doit(0, 0, LOG_ERR, fmt, ap);
    va_end(ap);
}

/*
 * Fatal error unrelated to a system call.
 * Print a message and terminate.
 */
void log_quit(const char *fmt, ...)
{
    va_list ap;

    va_start(ap, fmt);
    log_doit(0, 0, LOG_ERR, fmt, ap);
    va_end(ap);
    exit(2);
}

/*
 * Fatal error related to a system call.
 * Error number passed as an explicit parameter.
 * Print a message and terminate.
 */
void log_exit(int error, const char *fmt, ...)
{
    va_list ap;

    va_start(ap, fmt);
    log_doit(1, error, LOG_ERR, fmt, ap);
    va_end(ap);
    exit(2);
}

/*
 * Print a message and return to caller.
 * Caller specifies "errnoflag" and "priority".
 */
static void log_doit(int errnoflag, int error, int priority,
    const char *fmt, va_list ap)
{
    int len = 0;
    int left = 0;
    char *pos = NULL;
    char err[128] = {'\0'};
    char buf[MAXLINE] = {'\0'};

    pos = buf;
    left = MAXLINE;
    len = vsnprintf(pos, left, fmt, ap);
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
