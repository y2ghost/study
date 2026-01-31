#include <chtbl.h>
#include <stdlib.h>
#include <string.h>

int chtbl_init(chtbl_t *htbl, int buckets,
    int (*h)(const void *key),
    int (*match)(const void *key1, const void *key2),
    void (*destroy)(void*data))
{
    htbl->table = (list_t *)malloc(buckets * sizeof(list_t));
    if (NULL == htbl->table) {
       return -1;
    }
    
    htbl->buckets = buckets;
    for (int i=0; i<htbl->buckets; i++) {
       list_init(&htbl->table[i], destroy);
    }
    
    htbl->h = h;
    htbl->match = match;
    htbl->destroy = destroy;
    htbl->size = 0;
    return 0;
}

void chtbl_destroy(chtbl_t *htbl)
{
    for (int i=0; i<htbl->buckets; i++) {
       list_destroy(&htbl->table[i]);
    }
    
    free(htbl->table);
    memset(htbl, 0, sizeof(chtbl_t));
}

int chtbl_insert(chtbl_t *htbl, const void *data)
{
    void *temp = (void *)data;
    if (0 == chtbl_lookup(htbl, &temp)) {
       return 1;
    }
    
    int bucket = htbl->h(data) % htbl->buckets;
    int retval = list_ins_next(&htbl->table[bucket], NULL, data);

    if (0 == retval) {
       htbl->size++;
    }
    
    return retval;
}

int chtbl_remove(chtbl_t *htbl, void **data)
{
    int bucket = htbl->h(*data) % htbl->buckets;
    list_elm_t *prev = NULL;

    for (list_elm_t *element=list_head(&htbl->table[bucket]);
        NULL!=element; element=list_next(element)) {
        if (htbl->match(*data, list_data(element))) {
            if (0 == list_rem_next(&htbl->table[bucket], prev, data)) {
                htbl->size--;
                return 0;
            } else {
                return -1;
            }
       }
    
       prev = element;
    }
    
    return -1;
}

int chtbl_lookup(const chtbl_t *htbl, void **data)
{
    int bucket = htbl->h(*data) % htbl->buckets;
    for (list_elm_t *element=list_head(&htbl->table[bucket]);
        NULL!=element; element=list_next(element)) {
        if (htbl->match(*data, list_data(element))) {
            *data = list_data(element);
            return 0;
        }
    }
    
    return -1;
}
