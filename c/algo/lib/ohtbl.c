#include <ohtbl.h>
#include <stdlib.h>
#include <string.h>

static char vacated = '\0';

int ohtbl_init(ohtbl_t *htbl, int positions,
    int (*h1)(const void *key),
    int (*h2)(const void *key),
    int (*match)(const void *key1, const void *key2),
    void (*destroy)(void *data))
{
    htbl->table = (void **)malloc(positions * sizeof(void *));
    if (NULL == htbl->table) {
        return -1;
    }
    
    htbl->positions = positions;
    for (int i=0; i<htbl->positions; i++) {
        htbl->table[i] = NULL;
    }
    
    htbl->vacated = &vacated;
    htbl->h1 = h1;
    htbl->h2 = h2;
    htbl->match = match;
    htbl->destroy = destroy;
    htbl->size = 0;
    return 0;
}

void ohtbl_destroy(ohtbl_t *htbl)
{
    if (NULL != htbl->destroy) {
        for (int i=0; i<htbl->positions; i++) {
            if (NULL!=htbl->table[i] && htbl->table[i]!=htbl->vacated) {
                htbl->destroy(htbl->table[i]);
            }
        }
    }
    
    free(htbl->table);
    memset(htbl, 0, sizeof(ohtbl_t));
}

int ohtbl_insert(ohtbl_t *htbl, const void *data)
{
    if (htbl->size == htbl->positions) {
        return -1;
    }
    
    void *temp = (void *)data;
    if (0 == ohtbl_lookup(htbl, &temp)) {
        return 1;
    }
    
    for (int i=0; i<htbl->positions; i++) {
        int position = (htbl->h1(data) + (i * htbl->h2(data))) % htbl->positions;
        if (NULL==htbl->table[position] ||
            htbl->table[position] == htbl->vacated) {
            htbl->table[position] = (void *)data;
            htbl->size++;
            return 0;
        }
    }
    
    return -1;
}

int ohtbl_remove(ohtbl_t *htbl, void **data)
{
    for (int i=0; i<htbl->positions; i++) {
        int position = (htbl->h1(*data) + (i * htbl->h2(*data))) % htbl->positions;
        if (NULL == htbl->table[position]) {
            return -1;
        } else if (htbl->table[position] == htbl->vacated) {
            continue;
        } else if (htbl->match(htbl->table[position], *data)) {
            *data = htbl->table[position];
            htbl->table[position] = htbl->vacated;
            htbl->size--;
            return 0;
        }
    }
    
    return -1;
}

int ohtbl_lookup(const ohtbl_t *htbl, void **data)
{
    for (int i=0; i<htbl->positions; i++) {
        int position = (htbl->h1(*data) + (i * htbl->h2(*data))) % htbl->positions;
        if (NULL == htbl->table[position]) {
            return -1;
        } else if (htbl->match(htbl->table[position], *data)) {
            *data = htbl->table[position];
            return 0;
        }
    }
    
    return -1;
}
