#ifndef _FILE_ACCESSOR_H_
#define _FILE_ACCESSOR_H_

#include "buffer.h"
#include "array_list.h"
#include <stdio.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct file_accessor_t file_accessor_t;
typedef struct ferror_observer_t ferror_observer_t;

struct file_accessor_t {
    FILE *fp;
    const char *fname;
    const char *fmode;
    array_list_t observers;
    bool (* const processor)(struct file_accessor_t *self);
};

struct ferror_observer_t {
    void (* const report)(ferror_observer_t *self, file_accessor_t *accessor);
};

bool access_file(file_accessor_t *accessor);
FILE *get_file_pointer(file_accessor_t *accessor);
long file_size(file_accessor_t *accessor);
long file_current_pos(file_accessor_t *accessor);
int set_file_pos(file_accessor_t *accessor, long offset, int whence);
bool read_file(file_accessor_t *accessor, buf_ctx_t *buf_ctx);
bool write_file(file_accessor_t *accessor, buf_ctx_t *buf_ctx);

void add_file_error_observer(file_accessor_t *accessor,
    ferror_observer_t *ob);
void remove_file_error_observer(file_accessor_t *accessor,
    ferror_observer_t *ob);

#ifdef __cplusplus
}
#endif

#endif /* _FILE_ACCESSOR_H_ */
