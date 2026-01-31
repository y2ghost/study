#ifndef _BUFFER_H_
#define _BUFFER_H_

#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct buf_ctx_t buf_ctx_t;

struct buf_ctx_t {
    void *buf;
    size_t size;
    bool (* const processor)(struct buf_ctx_t *self);
};

bool buffer(buf_ctx_t *ctx);
void *allocate_buffer(buf_ctx_t *ctx, size_t size);

#ifdef __cplusplus
}
#endif

#endif /* _BUFFER_H_ */
