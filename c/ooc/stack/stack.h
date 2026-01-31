#ifndef _STACK_H_
#define _STACK_H_

#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct validator_t validator_t;
typedef struct range_validator_t range_validator_t;
typedef struct previous_validator_t previous_validator_t;
typedef struct chained_validator_t chained_validator_t;

/* the stack value basic validator */
struct validator_t {
    bool (* const validate)(struct validator_t *self, int val);
};

/* check the stack value range */
struct range_validator_t {
    validator_t base;
    const int min;
    const int max;
};

/* the stack value must bigger than the previous value */
struct previous_validator_t {
    validator_t base;
    int previous;
};

struct chained_validator_t {
    validator_t base;
    validator_t *wrapped;
    validator_t *next;
};

typedef struct mystack_t {
    int top;
    const size_t size;
    int * const buff;
    validator_t * const validator;
} mystack_t;

bool validate_range(validator_t *v, int val);
bool validate_previous(validator_t *v, int val);
bool validate_chain(validator_t *v, int val);

bool push(mystack_t *s, int val);
bool pop(mystack_t *s, int *val);

#define new_range_validator(min, max)   {{validate_range}, (min), (max)}
#define new_previous_validator          {{validate_previous}, 0}

#define new_chained_validator(wrapped, next) { \
    {validate_chain}, (wrapped), (next)}

#define new_stack_with_validator(buff, validator) { \
    0, sizeof(buff)/sizeof(*(buff)), (buff), (validator) \
}

#define new_stack(buff) new_stack_with_validator(buff, NULL)

#ifdef __cplusplus
}
#endif

#endif /* _STACK_H_ */
