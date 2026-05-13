#ifndef STACK_H
#define STACK_H

#include <list.h>

typedef list_t ystack_t;

#define stack_init  list_init
#define stack_destroy   list_destroy

int stack_push(ystack_t *stack, const void *data);
int stack_pop(ystack_t *stack, void **data);

#define stack_peek(stack)   (0==(stack)->head ? 0 : (stack)->head->data)
#define stack_size  list_size

#endif /* STACK_H */
