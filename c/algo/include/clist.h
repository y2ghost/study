#ifndef CLIST_H
#define CLIST_H

typedef struct clist_elm_t {
    void *data;
    struct clist_elm_t *next;
} clist_elm_t;

typedef struct clist_t {
    int size;
    int (*match)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    clist_elm_t *head;
} clist_t;

void clist_init(clist_t *list, void (*destroy)(void *data));
void clist_destroy(clist_t *list);
int clist_ins_next(clist_t *list, clist_elm_t *elm, const void *data);
int clist_rem_next(clist_t *list, clist_elm_t *elm, void **data);

#define clist_size(list)    ((list)->size)
#define clist_head(list)    ((list)->head)
#define clist_data(elm) ((elm)->data)
#define clist_next(elm) ((elm)->next)

#endif /* CLIST_H */
