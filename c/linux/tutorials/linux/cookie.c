#define _GNU_SOURCE
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#define INIT_BUF_SIZE   4

struct memfile_cookie {
    char *buf;
    size_t allocated;
    size_t endpos;
    off_t offset;
};

static ssize_t _memfile_write(void *c, const char *buf, size_t size)
{
    char *new_buff = NULL;
    struct memfile_cookie *cookie = c;

    while (1) {
        size_t new_off = size + cookie->offset;

        if (new_off <= cookie->allocated) {
            break;
        }

        new_buff = realloc(cookie->buf, cookie->allocated*2);
        if (NULL != new_buff) {
            cookie->allocated *= 2;
            cookie->buf = new_buff;
        } else {
            return -1;
        }
    }

    memcpy(cookie->buf+cookie->offset, buf, size);
    cookie->offset += size;
    if (cookie->offset > cookie->endpos) {
        cookie->endpos = cookie->offset;
    }

    return size;
}

static ssize_t _memfile_read(void *c, char *buf, size_t size)
{
    ssize_t xbytes = 0;
    struct memfile_cookie *cookie = c;

    xbytes = size;
    if (cookie->offset+size > cookie->endpos) {
        xbytes = cookie->endpos - cookie->offset;
    }

    if (xbytes < 0) {
        xbytes = 0;
    }

    memcpy(buf, cookie->buf+cookie->offset, xbytes);
    cookie->offset += xbytes;
    return xbytes;
}

static int _memfile_seek(void *c, off64_t *offset, int whence)
{
    off64_t new_offset = 0;
    struct memfile_cookie *cookie = c;

    switch (whence) {
    case SEEK_SET:
        new_offset = *offset;
        break;
    case SEEK_END:
        new_offset = cookie->endpos + *offset;
        break;
    case SEEK_CUR:
        new_offset = cookie->offset + *offset;
        break;
    default:
        return -1;
    }

    if (new_offset < 0) {
        return -1;
    }

    cookie->offset = new_offset;
    *offset = new_offset;
    return 0;
}

static int _memfile_close(void *c)
{
    struct memfile_cookie *cookie = c;

    free(cookie->buf);
    cookie->allocated = 0;
    cookie->buf = NULL;
    return 0;
}

int main(int ac, char **av)
{
    cookie_io_functions_t memfile_func = {
        .read = _memfile_read,
        .write = _memfile_write,
        .seek = _memfile_seek,
        .close = _memfile_close,
    };
    FILE *fp = NULL;
    struct memfile_cookie yycookie;
    ssize_t nread = 0;
    long p = 0;
    int j = 0;
    char buf[1000] = {'\0'};

    yycookie.buf = malloc(INIT_BUF_SIZE);
    if (NULL == yycookie.buf) {
        perror("malloc");
        exit(EXIT_FAILURE);
    }

    yycookie.allocated = INIT_BUF_SIZE;
    yycookie.offset = 0;
    yycookie.endpos = 0;

    fp = fopencookie(&yycookie, "w+", memfile_func);
    if (NULL == fp) {
        perror("fopencookie");
        exit(EXIT_FAILURE);
    }

    for (j=0; j<ac; ++j) {
        if (EOF == fputs(av[j], fp)) {
            perror("fputs");
            exit(EXIT_FAILURE);
        }
    }

    for (p=0; ; p+=5) {
        if (-1 == fseek(fp, p, SEEK_SET)) {
            perror("fseek");
            exit(EXIT_FAILURE);
        }

        nread = fread(buf, 1, 2, fp);
        if (-1 == nread) {
            perror("fread");
            exit(EXIT_FAILURE);
        }

        if (0 == nread) {
            printf("Reached end of file\n");
            break;
        }

        printf("/%.*s/\n", (int)nread, buf);
    }

    fclose(fp);
    fp = NULL;
    return 0;
}
