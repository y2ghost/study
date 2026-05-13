#include <event2/buffer.h>
#include <stdio.h>
#include <stdlib.h>

// 记住处理的字节数，演示evbuffer的使用方法
struct total_processed {
    size_t n;
};

void count_megabytes_cb(struct evbuffer *buffer,
    const struct evbuffer_cb_info *info, void *arg)
{
    struct total_processed *tp = arg;
    size_t old_n = tp->n;
    tp->n += info->n_deleted;
    int megabytes = ((tp->n) >> 20) - (old_n >> 20);

    for (int i=0; i<megabytes; ++i) {
        putc('.', stdout);
    }
}

void operation_with_counted_bytes(void)
{
    struct total_processed *tp = malloc(sizeof(*tp));
    struct evbuffer *buf = evbuffer_new();
    tp->n = 0;
    evbuffer_add_cb(buf, count_megabytes_cb, tp);
    // 处理完毕业务逻辑释放资源
    evbuffer_free(buf);
    free(tp);
}
