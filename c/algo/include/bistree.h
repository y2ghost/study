#ifndef BISTREE_H
#define BISTREE_H

#include "bitree.h"

#define AVL_LFT_HEAVY   1
#define AVL_BALANCED    0
#define AVL_RGT_HEAVY   -1

typedef struct avl_node_t {
    void *data;
    int hidden;
    int factor;
} avl_node_t;

typedef bitree_t bistree_t;

void bistree_init(bistree_t *tree,
    int (*compare)(const void *key1, const void *key2),
    void (*destroy)(void *data));
void bistree_destroy(bistree_t *tree);
int bistree_insert(bistree_t *tree, const void *data);
int bistree_remove(bistree_t *tree, const void *data);
int bistree_lookup(bistree_t *tree, void **data);

#define bistree_size(tree)  ((tree)->size)

#endif /* BISTREE_H */
