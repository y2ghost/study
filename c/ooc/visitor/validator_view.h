#ifndef _VALIDATOR_VIEW_H_
#define _VALIDATOR_VIEW_H_

#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

struct validator_t;

void print_validator(struct validator_t *v, char *buf, size_t size);

#ifdef __cplusplus
}
#endif

#endif /* _VALIDATOR_VIEW_H_ */
