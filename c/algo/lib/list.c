#include <list.h>
#include <stdlib.h>
#include <string.h>

void list_init(list_t *list, void (*destroy)(void *data))
{
    list->size = 0;
    list->destroy = destroy;
    list->head = NULL;
    list->tail = NULL;
}

void list_destroy(list_t *list)
{
    void *data = NULL;
    while (list_size(list) > 0) {
        if (0==list_rem_next(list,NULL,(void**)&data)
            && NULL!=list->destroy) {
            list->destroy(data);
        }
    }
    
    memset(list, 0, sizeof(*list));
}

int list_ins_next(list_t *list, list_elm_t *elm, const void *data)
{
    list_elm_t *new_elm = (list_elm_t*)malloc(sizeof(list_elm_t));
    if (NULL == new_elm) {
        return -1;
    }

    new_elm->data = (void*)data;
    if (NULL == elm) {
        if (0 == list_size(list)) {
            list->tail = new_elm;
        }
    
        new_elm->next = list->head;
        list->head = new_elm;
    } else {
        if (NULL == elm->next) {
            list->tail = new_elm;
        }
    
        new_elm->next = elm->next;
        elm->next = new_elm;
    }
    
    list->size++;
    return 0;
}

int list_rem_next(list_t *list, list_elm_t *elm, void **data)
{
    if (0 == list_size(list)) {
        return -1;
    }
    
    list_elm_t *old_elm = NULL;
    if (NULL == elm) {
        *data = list->head->data;
        old_elm = list->head;
        list->head = list->head->next;
    
        if (1 == list_size(list)) {
            list->tail = NULL;
        }
    } else {
        old_elm = elm->next;
        if (NULL == old_elm) {
            return -1;
        }
    
        *data = old_elm->data;
        elm->next = old_elm->next;
    
        if (NULL == elm->next) {
            list->tail = elm;
        }
    }
    
    free(old_elm);
    list->size--;
    return 0;
}
