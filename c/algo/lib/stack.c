#include <stack.h>
#include <stdlib.h>

int stack_push(ystack_t *stack, const void *data) {
    return list_ins_next((list_t*)stack, NULL, data);
}

int stack_pop(ystack_t *stack, void **data) {
    return list_rem_next((list_t*)stack, NULL, data);
}
