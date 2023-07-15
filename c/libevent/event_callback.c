#include <event2/event.h>

// 事件回调示例
void cb_func(evutil_socket_t fd, short what, void *arg)
{
    const char *data = arg;
    printf("Got an event on socket %d:%s%s%s%s [%s]",
        (int) fd,
        (what&EV_TIMEOUT) ? " timeout" : "",
        (what&EV_READ)    ? " read" : "",
        (what&EV_WRITE)   ? " write" : "",
        (what&EV_SIGNAL)  ? " signal" : "",
        data);
}

void main_loop(evutil_socket_t fd1, evutil_socket_t fd2)
{
    struct timeval five_seconds = {5,0};
    struct event_base *base = event_base_new();

    struct event *ev1 = event_new(base, fd1,
        EV_TIMEOUT|EV_READ|EV_PERSIST, cb_func,
        (char*)"读取event");
    struct event *ev2 = event_new(base, fd2,
        EV_WRITE|EV_PERSIST, cb_func,
        (char*)"写入event");

    event_add(ev1, &five_seconds);
    event_add(ev2, NULL);
    event_base_dispatch(base);
}

