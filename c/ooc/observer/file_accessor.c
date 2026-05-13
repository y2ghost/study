#include "file_accessor.h"
#include "array_list.h"
#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <errno.h>

static void _error_reort(file_accessor_t *accessor)
{
    int i = 0;;
    array_list_t *obs = &accessor->observers;

    for (i=0; i<obs->index; ++i) {
        ferror_observer_t *ob = (ferror_observer_t*)obs->get(obs, i);
        ob->report(ob, accessor);
    }
}

bool access_file(file_accessor_t *accessor)
{
    assert(accessor);
    bool rc = accessor->processor(accessor);

    if (NULL != accessor->fp) {
        if (0 != fclose(accessor->fp)) {
            _error_reort(accessor);
            rc = false;
        }
    }

    return rc;
}

FILE *get_file_pointer(file_accessor_t *accessor)
{
    assert(accessor);
    if (NULL == accessor->fp) {
        accessor->fp = fopen(accessor->fname, accessor->fmode);
        if (NULL == accessor->fp) {
            _error_reort(accessor);
        }
    }

    return accessor->fp;
}

long file_size(file_accessor_t *accessor)
{
    long save = file_current_pos(accessor);
    if (save < 0) {
        return -1;
    }

    if (0 != set_file_pos(accessor, 0, SEEK_END)) {
        return -1;
    }
    
    long size = file_current_pos(accessor);
    set_file_pos(accessor, save, SEEK_SET);
    return size;
}

long file_current_pos(file_accessor_t *accessor)
{
    assert(accessor);
    FILE *fp = get_file_pointer(accessor);

    if (NULL == fp) {
        return -1;
    }

    long rc = ftell(fp);
    if (rc < 0) {
        _error_reort(accessor);
    }

    return rc;
}

int set_file_pos(file_accessor_t *accessor, long offset, int whence)
{
    assert(accessor);
    FILE *fp = get_file_pointer(accessor);

    if (NULL == fp) {
        return -1;
    }

    int rc = fseek(fp, offset, whence);
    if (0 != rc) {
        _error_reort(accessor);
    }

    return rc;
}

bool read_file(file_accessor_t *accessor, buf_ctx_t *buf_ctx)
{
    FILE *fp = get_file_pointer(accessor);
    if (NULL == fp) {
        return false;
    }

    int rc = true;
    if (buf_ctx->size != fread(buf_ctx->buf, 1, buf_ctx->size, fp)) {
        _error_reort(accessor);
        rc = false;
    }

    return rc;
}

bool write_file(file_accessor_t *accessor, buf_ctx_t *buf_ctx)
{
    FILE *fp = get_file_pointer(accessor);
    if (NULL == fp) {
        return false;
    }

    int rc = true;
    if (buf_ctx->size != fwrite(buf_ctx->buf, 1, buf_ctx->size, fp)) {
        _error_reort(accessor);
        rc = false;
    }

    return rc;
}

void add_file_error_observer(file_accessor_t *accessor,
    ferror_observer_t *ob)
{
    array_list_t *obs = &accessor->observers;
    obs->add(obs, ob);
}

void remove_file_error_observer(file_accessor_t *accessor,
    ferror_observer_t *ob)
{
    array_list_t *obs = &accessor->observers;
    obs->remove(obs, ob);
}
