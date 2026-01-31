#include <clist.h>
#include <stdio.h>
#include <stdlib.h>

static void print_list(const clist_t *list)
{
    clist_elm_t *element = NULL;
    int *data = NULL;
    int size = 0;
    int i = 0;
    
    fprintf(stdout, "List size is %d (circling twice)\n", clist_size(list));
    size = clist_size(list);
    element = clist_head(list);
    i = 0;
    
    while (i < size * 2) {
        data = clist_data(element);
        fprintf(stdout, "list[%03d]=%03d\n", (i % size), *data);
        element = clist_next(element);
        i++; 
    }
}

int main(int argc, char **argv)
{
    clist_t list;
    clist_elm_t *element = NULL;
    int *data = NULL;
    int i = 0;
    
    clist_init(&list, free);
    element = clist_head(&list);
    
    for (i=0; i<10; i++) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }
    
        *data = i + 1;
        if (0 != clist_ins_next(&list, element, data)) {
            return 1;
        }
    
        if (NULL == element) {
            element = clist_next(clist_head(&list));
        } else {
            element = clist_next(element);
        }
    }
    
    print_list(&list);
    element = clist_head(&list);
    
    for (i=0; i<10; i++) {
        element = clist_next(element);
    }
    
    data = clist_data(element);
    fprintf(stdout, "Circling and removing an element after the one containing "
        "%03d\n",*data);
    
    if (0 != clist_rem_next(&list, element, (void **)&data)) {
        return 1;
    }
    
    free(data);
    print_list(&list);
    element = clist_head(&list);
    
    for (i=0; i<15; i++) {
        element = clist_next(element);
    }
    
    data = clist_data(element);
    fprintf(stdout, "Circling and inserting 011 after the element containing "
        "%03d\n", *data);
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 11;
    if (0 != clist_ins_next(&list, element, data)) {
        return 1;
    }
    
    print_list(&list);
    fprintf(stdout, "Destroying the list\n");
    clist_destroy(&list);
    return 0;
}
