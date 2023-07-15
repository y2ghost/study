#include <dlist.h>
#include <stdlib.h>
#include <string.h>

void dlist_init(dlist_t *list, void (*destroy)(void *data))
{
    list->size = 0;
    list->destroy = destroy;
    list->head = NULL;
    list->tail = NULL;
}

void dlist_destroy(dlist_t *list)
{
    void *data = NULL;
    while (dlist_size(list) > 0) {
        if (0==dlist_remove(list, dlist_tail(list), (void **)&data)
            && NULL!=list->destroy) {
            list->destroy(data);
        }
    }
    
    memset(list, 0, sizeof(dlist_t));
}

int dlist_ins_next(dlist_t *list, dlist_elm_t *elm, const void *data)
{
    if (NULL==elm && 0!=dlist_size(list)) {
        return -1;
    }
    
    dlist_elm_t *new_elm = (dlist_elm_t*)malloc(sizeof(dlist_elm_t));
    if (NULL == new_elm) {
        return -1;
    }
    
    new_elm->data = (void *)data;
    if (0 == dlist_size(list)) {
        list->head = new_elm;
        list->head->prev = NULL;
        list->head->next = NULL;
        list->tail = new_elm;
    } else {
        new_elm->next = elm->next;
        new_elm->prev = elm;
    
        if (NULL == elm->next) {
            list->tail = new_elm;
        } else {
            elm->next->prev = new_elm;
        }
    
        elm->next = new_elm;
    }
    
    list->size++;
    return 0;
}

int dlist_ins_prev(dlist_t *list, dlist_elm_t *elm, const void *data)
{
    if (NULL==elm && 0!=dlist_size(list)) {
        return -1;
    }
    
    dlist_elm_t *new_elm = (dlist_elm_t*)malloc(sizeof(dlist_elm_t));
    if (NULL == new_elm) {
        return -1;
    }
    
    new_elm->data = (void *)data;
    if (0 == dlist_size(list)) {
        list->head = new_elm;
        list->head->prev = NULL;
        list->head->next = NULL;
        list->tail = new_elm;
    } else {
        new_elm->next = elm;
        new_elm->prev = elm->prev;
       
        if (NULL == elm->prev) {
            list->head = new_elm;
        } else {
            elm->prev->next = new_elm;
        }
        
        elm->prev = new_elm;
    }
    
    list->size++;
    return 0;
}

int dlist_remove(dlist_t *list, dlist_elm_t *elm, void **data)
{
    if (NULL==elm || 0==dlist_size(list)) {
        return -1;
    }
    
    *data = elm->data;
    if (elm == list->head) {
        list->head = elm->next;
        if (NULL == list->head) {
            list->tail = NULL;
        } else {
            elm->next->prev = NULL;
        }
    } else {
        elm->prev->next = elm->next;
        if (NULL == elm->next) {
            list->tail = elm->prev;
        } else {
            elm->next->prev = elm->prev;
        }
    }
    
    free(elm);
    list->size--;
    return 0;
}
