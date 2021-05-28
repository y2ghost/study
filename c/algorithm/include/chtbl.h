#ifndef CHTBL_H
#define CHTBL_H

#include <list.h>

typedef struct chtbl_t {
    int buckets;
    int (*h)(const void *key);
    int (*match)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    int size;
    list_t *table;
} chtbl_t;

int chtbl_init(chtbl_t *htbl, int buckets,
    int (*h)(const void *key),
    int (*match)(const void *key1, const void *key2),
    void (*destroy)(void *data));
void chtbl_destroy(chtbl_t *htbl);
int chtbl_insert(chtbl_t *htbl, const void *data);
int chtbl_remove(chtbl_t *htbl, void **data);
int chtbl_lookup(const chtbl_t *htbl, void **data);

#define chtbl_size(htbl)    ((htbl)->size)

#endif /* CHTBL_H */
