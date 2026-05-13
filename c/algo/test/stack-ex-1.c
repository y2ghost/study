#include <list.h>
#include <stack.h>
#include <stdio.h>
#include <stdlib.h>

static void print_stack(const ystack_t *stack)
{
    list_elm_t *element = NULL;
    int *data = NULL;
    int size = 0;
    int i = 0;
    
    fprintf(stdout, "ystack_t size is %d\n", size = stack_size(stack));
    i = 0;
    element = list_head(stack);
    
    while (i < size) {
        data = list_data(element);
        fprintf(stdout, "stack[%03d]=%03d\n", i, *data);
        element = list_next(element);
        i++;
    }
}

int main(int argc, char **argv)
{
    ystack_t stack;
    int *data = NULL;
    int i = 0;
    
    stack_init(&stack, free);
    fprintf(stdout, "Pushing 10 elements\n");
    
    for (i=0; i<10; i++) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }
    
        *data = i + 1;
        if (0 != stack_push(&stack, data)) {
            return 1;
        }
    }
    
    print_stack(&stack);
    fprintf(stdout, "Popping 5 elements\n");
    
    for (i=0; i<5; i++) {
        if (0 == stack_pop(&stack, (void **)&data)) {
            free(data);
        } else {
            return 1;
        }
    }
    
    print_stack(&stack);
    fprintf(stdout, "Pushing 100 and 200\n");
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 100;
    if (0 != stack_push(&stack, data)) {
        return 1;
    }
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 200;
    if (0 != stack_push(&stack, data)) {
        return 1;
    }
    
    print_stack(&stack);
    data = stack_peek(&stack);

    if (NULL != data) {
        fprintf(stdout, "Peeking at the top element...Value=%03d\n", *data);
    } else {
        fprintf(stdout, "Peeking at the top element...Value=NULL\n");
    }
    
    print_stack(&stack);
    fprintf(stdout, "Popping all elements\n");
    
    while (stack_size(&stack) > 0) {
        if (0 == stack_pop(&stack, (void **)&data)) {
            free(data);
        }
    }
    
    data = stack_peek(&stack);
    if (NULL != data) {
        fprintf(stdout, "Peeking at an empty stack...Value=%03d\n", *data);
    } else {
        fprintf(stdout, "Peeking at an empty stack...Value=NULL\n");
    }
    
    print_stack(&stack);
    fprintf(stdout, "Destroying the stack\n");
    stack_destroy(&stack);
    return 0;
}
