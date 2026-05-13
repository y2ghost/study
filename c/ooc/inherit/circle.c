#include "circle.h"
#include "class.h"
#include "new.h"

#include <stdio.h>

static void *Circle_ctor (void *self_, va_list *app)
{
    struct Circle *self = ((const struct Class *) Point)->ctor(self_, app);
    self -> rad = va_arg(*app, int);
    return self;
}

static void Circle_draw (const void *self_)
{
    const struct Circle *self = self_;

    printf("circle at %d,%d rad %d\n", x(self), y(self), self->rad);
}

static const struct Class _Circle = {
    sizeof(struct Circle),
    Circle_ctor,
    NULL,
    Circle_draw
};

const void *Circle = &_Circle;
