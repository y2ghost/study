#include "stack.h"

static bool _is_stack_full(const mystack_t *s)
{
    return s->top == s->size;
}

static bool _is_stack_empty(const mystack_t *s)
{
    return 0 == s->top;
}

bool validate_range(validator_t *v, int val)
{
    range_validator_t *rv = (range_validator_t*)v;
    return val>=rv->min && val<=rv->max;
}

bool validate_previous(validator_t *v, int val)
{
    previous_validator_t *pv = (previous_validator_t*)v;
    if (val < pv->previous) {
        return false;
    }

    pv->previous = val;
    return true;
}

static bool _validate(validator_t *v, int val)
{
    if (NULL == v) {
        return true;
    }

    return v->validate(v, val);
}

bool push(mystack_t *s, int val)
{
    if (NULL==s || true==_is_stack_full(s)
        || false==_validate(s->validator, val)) {
        return false;
    }

    s->buff[s->top++] = val;
    return true;
}

bool pop(mystack_t *s, int *val)
{
    if (NULL==s || NULL==val || true==_is_stack_empty(s)) {
        return false;
    }

    *val = s->buff[--s->top];
    return true;
}

bool validate_chain(validator_t *v, int val)
{
    chained_validator_t *chain = (chained_validator_t*)v;
    validator_t *cv = chain->wrapped;

    if (false == cv->validate(cv, val)) {
        return false;
    }

    int rc = true;
    cv = chain->next;

    if (NULL != cv) {
        rc = cv->validate(cv, val);
    }

    return rc;
}
