#include "common.h"
#include <errno.h>
#include <stdarg.h>

static void err_doit(int, int, const char *, va_list);

// 打印系统调用错误信息并返回
void err_ret(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    err_doit(1, errno, fmt, ap);
    va_end(ap);
}

// 打印系统调用错误信息并退出
void err_sys(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    err_doit(1, errno, fmt, ap);
    va_end(ap);
    exit(1);
}

// 打印error值对应的错误信息并返回
void err_cont(int error, const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    err_doit(1, error, fmt, ap);
    va_end(ap);
}

// 打印error值对应的错误信息并返回
void err_exit(int error, const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    err_doit(1, error, fmt, ap);
    va_end(ap);
    exit(1);
}

// 打印错误信息、转存CORE、退出程序
void err_dump(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    err_doit(1, errno, fmt, ap);
    va_end(ap);
    abort();
    exit(1);
}

// 打印错误信息并返回 - 适合非系统调用函数
void err_msg(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    err_doit(0, 0, fmt, ap);
    va_end(ap);
}

// 打印错误信息并退出 - 适合非系统调用函数
void err_quit(const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    err_doit(0, 0, fmt, ap);
    va_end(ap);
    exit(1);
}

static void err_doit(int errnoflag, int error, const char *fmt, va_list ap)
{
    char err[128] = {'\0'};
    char buf[MAXLINE] = {'\0'};
    char *pos = buf;
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
    fflush(stderr);
    fputs(buf, stderr);
    fflush(NULL);
}

