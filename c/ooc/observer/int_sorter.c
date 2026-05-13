#include "int_sorter.h"
#include "file_accessor.h"
#include "buffer.h"
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>

typedef struct sort_ctx_t sort_ctx_t;
typedef struct mybuff_t mybuff_t;
typedef struct myfile_t myfile_t;
typedef struct mysize_t mysize_t;

struct sort_ctx_t {
    const char *fname;
    enum sort_error_e error;
};

struct mybuff_t {
    buf_ctx_t base;
    sort_ctx_t *sort_ctx;
};

struct myfile_t {
    file_accessor_t base;
    mybuff_t *mybuff;
};

struct mysize_t {
    file_accessor_t base;
    long size;
};

static bool _reader(file_accessor_t *accessor);
static bool _do_with_buffer(buf_ctx_t *buf_ctx);
static bool _writer(file_accessor_t *accessor);
static int _comparator(const void *p1, const void *p2);
static void _file_error(ferror_observer_t *ob, file_accessor_t *accessor);

static ferror_observer_t file_error_observer = {
     _file_error,
};

enum sort_error_e int_sorter(const char *fname)
{
    sort_ctx_t ctx = {fname, ERR_CAT_OK};
    mybuff_t mybuff = {{NULL, 0, _do_with_buffer}, &ctx};
    buffer(&mybuff.base);
    return ctx.error;
}

static bool _do_with_buffer(buf_ctx_t *buf_ctx)
{
    void *observers[4] = {0};
    mybuff_t *mybuff = (mybuff_t*)buf_ctx;
    const char *fname = mybuff->sort_ctx->fname;
    myfile_t read_ctx = {
        {NULL, fname, "rb", new_array_list(observers), _reader},
        mybuff,
    };

    add_file_error_observer(&read_ctx.base, &file_error_observer);
    if (false == access_file(&read_ctx.base)) {
        return false;
    }

    qsort(buf_ctx->buf, buf_ctx->size/sizeof(int), sizeof(int), _comparator);
    myfile_t write_ctx = {
        {NULL,fname, "wb", new_array_list(observers), _writer},
        mybuff,
    };

    add_file_error_observer(&write_ctx.base, &file_error_observer);
    return access_file(&write_ctx.base);
}

static bool _reader(file_accessor_t *accessor)
{
    myfile_t *myfile = (myfile_t*)accessor;
    long size = file_size(accessor);

    if (-1 == size) {
        return false;
    }

    if (NULL == allocate_buffer(&myfile->mybuff->base, size)) {
        myfile->mybuff->sort_ctx->error = ERR_CAT_MEMORY;
        return false;
    }

    return read_file(accessor, &myfile->mybuff->base);
}

static bool _writer(file_accessor_t *accessor)
{
    myfile_t *myfile = (myfile_t*)accessor;
    return write_file(accessor, &myfile->mybuff->base);
}

static int _comparator(const void *p1, const void *p2)
{
    int i1 = *(const int *)p1;
    int i2 = *(const int *)p2;
    int rc = 0;

    if (i1 < i2) {
        rc = -1;
    } else if (i1 > i2) {
        rc = 1;
    }

    return rc;
}

static void _file_error(ferror_observer_t *ob, file_accessor_t *accessor)
{
    fprintf(stderr, "File access error '%s'(mode: %s): %s\n",
        accessor->fname, accessor->fmode, strerror(errno));
    myfile_t *myfile = (myfile_t*)accessor;
    myfile->mybuff->sort_ctx->error = ERR_CAT_FILE;
}
