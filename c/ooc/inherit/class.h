#ifndef YY_CLASS_H
#define YY_CLASS_H

#include <stdarg.h>
#include <sys/types.h>

struct Class {
    size_t size;
    void *(*ctor)(void *self, va_list *app);
    void *(*dtor)(void *self);
    void (*draw)(const void *self);
};

struct Point {
    const void *class;
    int x;
    int y;
};

#define x(p)    (((const struct Point *)(p))->x)
#define y(p)    (((const struct Point *)(p))->y)

struct Circle { const struct Point _; int rad; };

#endif /* YY_CLASS_H */
