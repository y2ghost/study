#ifndef BITREE_H
#define BITREE_H

typedef struct bitree_node_t {
    void *data;
    struct bitree_node_t *left;
    struct bitree_node_t *right;
} bitree_node_t;

typedef struct bitree_t {
    int size;
    int (*compare)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    bitree_node_t *root;
} bitree_t;

void bitree_init(bitree_t *tree, void (*destroy)(void *data));
void bitree_destroy(bitree_t *tree);
int bitree_ins_left(bitree_t *tree, bitree_node_t *node, const void *data);
int bitree_ins_right(bitree_t *tree, bitree_node_t *node, const void *data);
void bitree_rem_left(bitree_t *tree, bitree_node_t *node);
void bitree_rem_right(bitree_t *tree, bitree_node_t *node);
int bitree_merge(bitree_t *merge, bitree_t *left, bitree_t *right, const void *data);

#define bitree_size(tree)   ((tree)->size)
#define bitree_root(tree)   ((tree)->root)
#define bitree_is_eob(node) (0 == (node))
#define bitree_is_leaf(node)    (0==(node)->left && 0==(node)->right)
#define bitree_data(node)   ((node)->data)
#define bitree_left(node)   ((node)->left)
#define bitree_right(node)  ((node)->right)

#endif /* BITREE_H */
