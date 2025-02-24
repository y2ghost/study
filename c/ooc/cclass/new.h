#ifndef _YY_NEW_H_
#define _YY_NEW_H_

#include <stddef.h>

void *new(const void *class, ...);
void delete(void *item);
void *clone(const void *self);
int differ(const void *self, const void *b);
size_t sizeOf(const void *self);

#endif /* _YY_NEW_H_ */
