#include "class.h"

#include <assert.h>
#include <stdlib.h>

void *new(const void*class_, ...)
{
    const struct Class *class = class_;
	void *p = calloc(1, class->size);

	assert(NULL != p);
	*(const struct Class **)p = class;

	if (NULL != class->ctor) {
        va_list ap;

		va_start(ap, class_);
		p = class->ctor(p, & ap);
		va_end(ap);
	}

	return p;
}

void delete(void *self)
{
    const struct Class **cp = self;

	if (NULL!=self && NULL!=*cp && NULL!=(*cp)->dtor) {
        self = (*cp)->dtor(self);
    }

	free(self);
}

void draw(const void *self)
{
    const struct Class *const *cp = self;

	assert(NULL!=self && NULL!=*cp && NULL!=(*cp)->draw);
	(*cp)->draw(self);
}
