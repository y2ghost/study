#include "str.h"
#include "new.h"
#include "class.h"

#include <assert.h>
#include <stdlib.h>
#include <string.h>

struct String {
    const void *class;
    char *text;
    struct String *next;
    unsigned int count;
};

/* of all strings */
static struct String *ring = NULL;

static void *string_ctor(void *self_, va_list *app)
{
    struct String *self = self_;
    const char *text = va_arg(*app, const char*);

    if (NULL != ring) {
        struct String *p = ring;

        do {
            if (0 == strcmp(p->text, text)) {
                ++p->count;
                free(self);
                return p;
            }

            p = p->next;
        } while (p != ring);
    } else {
        ring = self;
    }
    
    self->next = ring->next;
    ring->next = self;
    self->count = 1;
    self->text = malloc(strlen(text) + 1);
    assert(NULL != self->text);
    strcpy(self->text, text);
    return self;
}

static void *string_dtor(void *self_)
{
    struct String *self = self_;

    if (--self->count > 0) {
        return 0;
    }

    assert(NULL != ring);
    if (ring == self) {
        ring = self->next;
    }

    if (ring == self) {
        ring = NULL;
    } else {
        struct String *p = ring;
        while (self != p->next) {
            p = p->next;
            assert(p != ring);
        }

        p->next = self->next;
    }
    
    free(self->text);
    self->text = NULL;
    return self;
}

static void *string_clone(const void *self_)
{
    struct String *self = (void *)self_;

    ++self->count;
    return self;
}

static int string_differ(const void *self, const void *b)
{
    return self != b;
}

static const struct Class _String = {
    sizeof(struct String),
    string_ctor,
    string_dtor,
    string_clone,
    string_differ
};

const void *String = &_String;
