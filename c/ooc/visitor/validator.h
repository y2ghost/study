#ifndef _VALIDATOR_H_
#define _VALIDATOR_H_

#include <stddef.h>
#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct validator_t validator_t;
typedef struct range_validator_t range_validator_t;
typedef struct previous_validator_t previous_validator_t;
typedef struct validator_visitor_t validator_visitor_t;

struct validator_t {
    bool (* const validate)(struct validator_t *self, int val);
    void (* const accept)(struct validator_t *self,
        struct validator_visitor_t *pVisitor);
};

struct range_validator_t {
    validator_t base;
    const int min;
    const int max;
};

struct previous_validator_t {
    validator_t base;
    int previous;
};

struct validator_visitor_t {
    void (* const visit_range)(struct validator_visitor_t *self,
        range_validator_t *rv);
    void (* const visit_previous)(struct validator_visitor_t *self,
        previous_validator_t *pv);
};

void accept_range(validator_t *v, validator_visitor_t *visitor);
void accept_previous(validator_t *v, validator_visitor_t *visitor);

bool validate_range(validator_t *v, int val);
bool validate_previous(validator_t *v, int val);

#define new_range_validator(min, max) \
    {{validate_range, accept_range}, (min), (max)}
#define new_previous_validator  {{validate_previous, accept_previous}, 0}

#ifdef __cplusplus
}
#endif

#endif /* _VALIDATOR_H_ */
