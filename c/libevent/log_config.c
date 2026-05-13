#include <event2/event.h>
#include <stdio.h>

// 示例配置日志处理方法
static void discard_cb(int severity, const char *msg)
{
    /* 不做任何日志处理 */
}

static FILE *logfile = NULL;
static void write_to_file_cb(int severity, const char *msg)
{
    if (!logfile) {
        return;
    }

    const char *s = "?";
    switch (severity) {
        case EVENT_LOG_DEBUG: s = "debug"; break;
        case EVENT_LOG_MSG:   s = "msg";   break;
        case EVENT_LOG_WARN:  s = "warn";  break;
        case EVENT_LOG_ERR:   s = "error"; break;
        default:              s = "?";     break;
    }

    fprintf(logfile, "[%s] %s\n", s, msg);
}

// 关闭libevent的日志功能
void suppress_logging(void)
{
    event_set_log_callback(discard_cb);
}

// 日志写入文件
void set_logfile(FILE *f)
{
    logfile = f;
    event_set_log_callback(write_to_file_cb);
}
