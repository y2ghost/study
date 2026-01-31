#include <event2/bufferevent.h>
#include <event2/buffer.h>
#include <ctype.h>

// buffer事件回调示例
void read_callback_uppercase(struct bufferevent *bev, void *ctx)
{
    char tmp[128] = {0};
    while (1) {
        size_t n = bufferevent_read(bev, tmp, sizeof(tmp));
        if (n <= 0) {
            break;
        }

        for (int i=0; i<n; ++i) {
            tmp[i] = toupper(tmp[i]);
        }

        bufferevent_write(bev, tmp, n);
    }
}

struct proxy_info {
    struct bufferevent *other_bev;
};

void read_callback_proxy(struct bufferevent *bev, void *ctx)
{
    struct proxy_info *inf = ctx;
    bufferevent_read_buffer(bev, bufferevent_get_output(inf->other_bev));
}

struct count {
    unsigned long last_fib[2];
};

void write_callback_fibonacci(struct bufferevent *bev, void *ctx)
{
    struct count *c = ctx;
    struct evbuffer *tmp = evbuffer_new();

    while (evbuffer_get_length(tmp) < 1024) {
        unsigned long next = c->last_fib[0] + c->last_fib[1];
        c->last_fib[0] = c->last_fib[1];
        c->last_fib[1] = next;
        evbuffer_add_printf(tmp, "%lu", next);
    }

    bufferevent_write_buffer(bev, tmp);
    evbuffer_free(tmp);
}

