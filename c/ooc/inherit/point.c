#include "point.h"
#include "class.h"
#include "new.h"

#include <stdio.h>

static void *Point_ctor(void *self_, va_list *app)
{
    struct Point *self = self_;

	self->x = va_arg(*app, int);
	self->y = va_arg(*app, int);
	return self;
}

static void Point_draw(const void *self_)
{
    const struct Point *self = self_;

	printf("\".\" at %d,%d\n", self->x, self->y);
}

static const struct Class _Point = {
	sizeof(struct Point),
    Point_ctor,
    NULL,
    Point_draw
};

const void *Point = &_Point;

void move (void *self_, int dx, int dy)
{
    struct Point *self = self_;

	self->x += dx;
    self->y += dy;
}
