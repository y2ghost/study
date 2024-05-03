#ifndef OHTBL_H
#define OHTBL_H

#include <stdlib.h>

typedef struct ohtbl_t {
    int positions;
    void *vacated;
    int (*h1)(const void *key);
    int (*h2)(const void *key);
    int (*match)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    int size;
    void **table;
} ohtbl_t;

int ohtbl_init(ohtbl_t *htbl, int positions,
    int (*h1)(const void *key),
    int (*h2)(const void *key),
    int (*match)(const void *key1, const void *key2),
    void (*destroy)(void *data));
void ohtbl_destroy(ohtbl_t *htbl);
int ohtbl_insert(ohtbl_t *htbl, const void *data);
int ohtbl_remove(ohtbl_t *htbl, void **data);
int ohtbl_lookup(const ohtbl_t *htbl, void **data);

#define ohtbl_size(htbl)    ((htbl)->size)

#endif /* OHTBL_H */
