#ifndef LIST_H
#define LIST_H

typedef struct list_elm_t {
    void *data;
    struct list_elm_t *next;
} list_elm_t;

typedef struct list_t {
    int size;
    int (*match)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    struct list_elm_t *head;
    struct list_elm_t *tail;
} list_t;

void list_init(list_t *list, void (*destroy)(void *data));
void list_destroy(list_t *list);
int list_ins_next(list_t *list, list_elm_t *elm, const void *data);
int list_rem_next(list_t *list, list_elm_t *elm, void **data);

#define list_size(list) ((list)->size)
#define list_head(list) ((list)->head)
#define list_tail(list) ((list)->tail)
#define list_is_head(list, elm) ((elm) == (list)->head)
#define list_is_tail(elm)   (0 == (elm)->next)
#define list_data(elm)      ((elm)->data)
#define list_next(elm)      ((elm)->next)

#endif /* LIST_H */
