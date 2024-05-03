#ifndef _YY_CLASS_H_
#define _YY_CLASS_H_

#include <stdarg.h>
#include <sys/types.h>

struct Class {
    size_t size;
    void *(*ctor)(void *self, va_list *app);
    void *(*dtor)(void *self);
    void *(*clone)(const void *self);
    int (*differ)(const void *self, const void *b);
};

#endif /* _YY_CLASS_H_ */
