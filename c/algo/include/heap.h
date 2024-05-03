#ifndef HEAP_H
#define HEAP_H

typedef struct heap_t {
    int size;
    int (*compare)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    void **tree;
} heap_t;

void heap_init(heap_t *heap,
    int (*compare)(const void *key1, const void *key2),
    void (*destroy)(void *data));
void heap_destroy(heap_t *heap);
int heap_insert(heap_t *heap, const void *data);
int heap_extract(heap_t *heap, void **data);

#define heap_size(heap) ((heap)->size)

#endif /* HEAP_H */
