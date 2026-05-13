#include <dlist.h>
#include <stdio.h>
#include <stdlib.h>

void print_list(const dlist_t *list)
{
    dlist_elm_t *element = NULL;
    int *data = NULL;
    int i = 0;
    
    fprintf(stdout, "List size is %d\n", dlist_size(list));
    i = 0;
    element = dlist_head(list);
    
    while (1) {
        data = dlist_data(element);
        fprintf(stdout, "list[%03d]=%03d\n", i, *data);
        i++;
    
        if (dlist_is_tail(element)) {
            break;
        } else {
            element = dlist_next(element);
        }
    }
}

int main(int argc, char **argv)
{
    dlist_t list;
    dlist_elm_t *element = NULL;
    int *data = NULL;
    int i = 0;
    
    dlist_init(&list, free);
    element = dlist_head(&list);
    
    for (i=10; i>0; i--) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }

        *data = i;
        if (0 != dlist_ins_prev(&list, dlist_head(&list), data)) {
            return 1;
        }
    }
    
    print_list(&list);
    element = dlist_head(&list);
    
    for (i=0; i<8; i++) {
        element = dlist_next(element);
    }
    
    data = dlist_data(element);
    fprintf(stdout, "Removing an element after the one containing %03d\n", *data);
    
    if (0 != dlist_remove(&list, element, (void **)&data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 011 at the tail of the list\n");
    *data = 11;
    
    if (0 != dlist_ins_next(&list, dlist_tail(&list), data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Removing an element at the tail of the list\n");
    element = dlist_tail(&list);
    
    if (0 != dlist_remove(&list, element, (void **)&data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 012 just before the tail of the list\n");
    *data = 12;
    
    if (0 != dlist_ins_prev(&list, dlist_tail(&list), data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Iterating and removing the fourth element\n");
    element = dlist_head(&list);
    element = dlist_next(element);
    element = dlist_prev(element);
    element = dlist_next(element);
    element = dlist_prev(element);
    element = dlist_next(element);
    element = dlist_next(element);
    element = dlist_next(element);
    
    if (0 != dlist_remove(&list, element, (void **)&data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 013 before the first element\n");
    *data = 13;
    
    if (0 != dlist_ins_prev(&list, dlist_head(&list), data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Removing an element at the head of the list\n");
    
    if (0 != dlist_remove(&list, dlist_head(&list), (void **)&data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 014 just after the head of the list\n");
    *data = 14;
    
    if (0 != dlist_ins_next(&list, dlist_head(&list), data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 015 two elements after the head of the list\n");
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 15;
    element = dlist_head(&list);
    element = dlist_next(element);
    
    if (0 != dlist_ins_next(&list, element, data)) {
        return 1;
    }
    
    print_list(&list);
    i = dlist_is_head(dlist_head(&list));
    fprintf(stdout, "Testing dlist_is_head...Value=%d (1=OK)\n", i);
    i = dlist_is_head(dlist_tail(&list));
    fprintf(stdout, "Testing dlist_is_head...Value=%d (0=OK)\n", i);
    i = dlist_is_tail(dlist_tail(&list));
    fprintf(stdout, "Testing dlist_is_tail...Value=%d (1=OK)\n", i);
    i = dlist_is_tail(dlist_head(&list));
    fprintf(stdout, "Testing dlist_is_tail...Value=%d (0=OK)\n", i);
    fprintf(stdout, "Destroying the list\n");
    dlist_destroy(&list);
    return 0;
}
