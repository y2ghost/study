#include "validator.h"
#include <stdio.h>

bool validate_range(validator_t *v, int val)
{
    range_validator_t *rv = (range_validator_t*)v;
    return rv->min <= val && val <= rv->max;
}

bool validate_previous(validator_t *v, int val)
{
    previous_validator_t *pv = (previous_validator_t *)v;
    if (val < pv->previous) {
        return false;
    }

    pv->previous = val;
    return true;
}

void accept_range(validator_t *v, validator_visitor_t *visitor)
{
    visitor->visit_range(visitor, (range_validator_t*)v);
}

void accept_previous(validator_t *v, validator_visitor_t *visitor)
{
    visitor->visit_previous(visitor, (previous_validator_t*)v);
}
