#ifndef DLIST_H
#define DLIST_H

typedef struct dlist_elm_t {
    void *data;
    struct dlist_elm_t *prev;
    struct dlist_elm_t *next;
} dlist_elm_t;

typedef struct dlist_t_ {
    int size;
    int (*match)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    dlist_elm_t *head;
    dlist_elm_t *tail;
} dlist_t;

void dlist_init(dlist_t *list, void (*destroy)(void *data));
void dlist_destroy(dlist_t *list);
int dlist_ins_next(dlist_t *list, dlist_elm_t *elm, const void *data);
int dlist_ins_prev(dlist_t *list, dlist_elm_t *elm, const void *data);
int dlist_remove(dlist_t *list, dlist_elm_t *elm, void **data);

#define dlist_size(list)    ((list)->size)
#define dlist_head(list)    ((list)->head)
#define dlist_tail(list)    ((list)->tail)
#define dlist_is_head(elm)  (0 == (elm)->prev)
#define dlist_is_tail(elm)  (0 == (elm)->next)
#define dlist_data(elm) ((elm)->data)
#define dlist_next(elm) ((elm)->next)
#define dlist_prev(elm) ((elm)->prev)

#endif /* DLIST_H */
