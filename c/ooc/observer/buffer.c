#include "buffer.h"
#include <assert.h>
#include <stdlib.h>

bool buffer(buf_ctx_t *ctx)
{
    bool rc = ctx->processor(ctx);
    free(ctx->buf);
    return rc;
}

void *allocate_buffer(buf_ctx_t *ctx, size_t size)
{
    assert(ctx->buf == NULL);
    ctx->buf = malloc(size);
    ctx->size = size;
    return ctx->buf;
}
