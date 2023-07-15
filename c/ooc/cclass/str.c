#include "new.h"
#include "str.h"
#include "class.h"

#include <assert.h>
#include <stdlib.h>
#include <string.h>

struct String {
    const void *class;
    char *text;
};

static void *string_ctor(void *self_, va_list *app)
{
    struct String *self = self_;
    const char *text = va_arg(*app, const char *);

    self->text = malloc(strlen(text) + 1);
    assert(NULL != self->text);
    strcpy(self->text, text);
    return self;
}

static void *string_dtor(void *self_)
{
    struct String *self = self_;

    free(self->text);
    self->text = NULL;
    return self;
}

static void *string_clone(const void *self_)
{
    const struct String *self = self_;

    return new(String, self->text);
}

static int string_differ(const void *self_, const void *_b)
{
    const struct String *self = self_;
    const struct String *b = _b;

    if (self == b) {
        return 0;
    }
    if (NULL == b || b->class != String) {
        return 1;
    }

    return strcmp(self->text, b->text);
}

static const struct Class _String = {
    sizeof(struct String),
    string_ctor,
    string_dtor,
    string_clone,
    string_differ
};

const void *String = &_String;
