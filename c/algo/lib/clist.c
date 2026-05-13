#include <clist.h>
#include <stdlib.h>
#include <string.h>

void clist_init(clist_t *list, void (*destroy)(void *data))
{
    list->size = 0;
    list->destroy = destroy;
    list->head = NULL;
}

void clist_destroy(clist_t *list)
{
    void *data = NULL;
    while (clist_size(list) > 0) {
        if (0==clist_rem_next(list, list->head, (void **)&data)
            && NULL!=list->destroy) {
            list->destroy(data);
        }
    }
    
    memset(list, 0, sizeof(clist_t));
    return;
}

int clist_ins_next(clist_t *list, clist_elm_t *elm, const void *data)
{
    clist_elm_t *new_elm = (clist_elm_t*)malloc(sizeof(clist_elm_t));
    if (NULL == new_elm) {
       return -1;
    }
    
    new_elm->data = (void *)data;
    if (0 == clist_size(list)) {
        new_elm->next = new_elm;
        list->head = new_elm;
    } else {
        new_elm->next = elm->next;
        elm->next = new_elm;
    }
    
    list->size++;
    return 0;
}

int clist_rem_next(clist_t *list, clist_elm_t *elm, void **data)
{
    if (0 == clist_size(list)) {
       return -1;
    }
    
    clist_elm_t *old_elm = elm->next;
    *data = old_elm->data;

    if (old_elm == elm) {
        list->head = NULL;
    } else {
        elm->next = old_elm->next;
    }
    
    free(old_elm);
    list->size--;
    return 0;
}
