#include "validator.h"
#include "validator_view.h"
#include <stdio.h>

static void range_view(validator_visitor_t *visitor,
    range_validator_t *rv);
static void previous_view(validator_visitor_t *visitor,
    previous_validator_t *pv);

typedef struct view_visitor_t {
    validator_visitor_t base;
    char *buf;
    size_t size;
} view_visitor_t;

void print_validator(validator_t *v, char *buf, size_t size)
{
    view_visitor_t visitor = {{range_view, previous_view}, buf, size};
    v->accept(v, &visitor.base);
}

static void range_view(validator_visitor_t *visitor,
    range_validator_t *rv)
{
    view_visitor_t *view = (view_visitor_t*)visitor;
    snprintf(view->buf, view->size, "Range(%d-%d)", rv->min, rv->max);
}

static void previous_view(validator_visitor_t *visitor,
    previous_validator_t *pv)
{
    view_visitor_t *view = (view_visitor_t*)visitor;
    snprintf(view->buf, view->size, "Previous");
}
