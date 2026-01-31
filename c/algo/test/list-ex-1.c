#include <list.h>
#include <stdio.h>
#include <stdlib.h>

static void print_list(const list_t *list)
{
    list_elm_t *element = NULL;
    int *data = NULL;
    int i = 0;
    
    fprintf(stdout, "list_t size is %d\n", list_size(list));
    i = 0;
    element = list_head(list);
    
    while (1) {
        data = list_data(element);
        fprintf(stdout, "list[%03d]=%03d\n", i, *data);
        i++;
    
        if (list_is_tail(element)) {
            break;
        } else {
            element = list_next(element);
        }
    }
}

int main(int argc, char **argv)
{
    list_t list;
    list_elm_t *element = NULL;
    int *data = NULL;
    int i = 0;
    
    list_init(&list, free);
    element = list_head(&list);
    
    for (i=10; i>0; i--) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }
    
        *data = i;
        if (0 != list_ins_next(&list, NULL, data)) {
            return 1;
        }
    }
    
    print_list(&list);
    element = list_head(&list);
    
    for (i=0; i<7; i++) {
        element = list_next(element);
    }
    
    data = list_data(element);
    fprintf(stdout, "Removing an element after the one containing %03d\n", *data);
    
    if (0 != list_rem_next(&list, element, (void **)&data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 011 at the tail of the list\n");
    *data = 11;

    if (0 != list_ins_next(&list, list_tail(&list), data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Removing an element after the first element\n");
    element = list_head(&list);

    if (0 != list_rem_next(&list, element, (void **)&data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 012 at the head of the list\n");
    *data = 12;

    if (0 != list_ins_next(&list, NULL, data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Iterating and removing the fourth element\n");
    element = list_head(&list);
    element = list_next(element);
    element = list_next(element);
    
    if (0 != list_rem_next(&list, element, (void **)&data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Inserting 013 after the first element\n");
    *data = 13;

    if (0 != list_ins_next(&list, list_head(&list), data)) {
        return 1;
    }
    
    print_list(&list);
    i = list_is_head(&list, list_head(&list));
    fprintf(stdout, "Testing list_is_head...Value=%d (1=OK)\n", i);
    i = list_is_head(&list, list_tail(&list));
    fprintf(stdout, "Testing list_is_head...Value=%d (0=OK)\n", i);
    i = list_is_tail(list_tail(&list));
    fprintf(stdout, "Testing list_is_tail...Value=%d (1=OK)\n", i);
    i = list_is_tail(list_head(&list));
    fprintf(stdout, "Testing list_is_tail...Value=%d (0=OK)\n", i);
    fprintf(stdout, "Destroying the list\n");
    list_destroy(&list);
    return 0;
}
