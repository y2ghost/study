#include <bitree.h>
#include <stdlib.h>
#include <string.h>

void bitree_init(bitree_t *tree, void (*destroy)(void *data))
{
    tree->size = 0;
    tree->destroy = destroy;
    tree->root = NULL;
}

void bitree_destroy(bitree_t *tree)
{
    bitree_rem_left(tree, NULL);
    memset(tree, 0x0, sizeof(bitree_t));
}

int bitree_ins_left(bitree_t *tree, bitree_node_t *node, const void *data)
{
    bitree_node_t **position = NULL;
    if (NULL == node) {
        if (bitree_size(tree) > 0) {
            return -1;
        }

        position = &tree->root;
    } else {
        if (NULL != bitree_left(node)) {
            return -1;
        }

        position = &node->left;
    }

    bitree_node_t *new_node = (bitree_node_t *)malloc(sizeof(*new_node));
    if (NULL == new_node) {
        return -1;
    }

    new_node->data = (void *)data;
    new_node->left = NULL;
    new_node->right = NULL;
    *position = new_node;
    tree->size++;
    return 0;
}

int bitree_ins_right(bitree_t *tree, bitree_node_t *node, const void *data)
{
    bitree_node_t **position = NULL;
    if (NULL == node) {
        if (bitree_size(tree) > 0) {
            return -1;
        }

        position = &tree->root;
    } else {
        if (NULL != bitree_right(node)) {
            return -1;
        }

        position = &node->right;
    }

    bitree_node_t *new_node = (bitree_node_t *)malloc(sizeof(*new_node));
    if (NULL == new_node) {
        return -1;
    }

    new_node->data = (void *)data;
    new_node->left = NULL;
    new_node->right = NULL;
    *position = new_node;
    tree->size++;
    return 0;
}

void bitree_rem_left(bitree_t *tree, bitree_node_t *node)
{
    if (0 == bitree_size(tree)) {
        return;
    }

    bitree_node_t **position = NULL;
    if (NULL == node) {
        position = &tree->root;
    } else {
        position = &node->left;
    }

    if (NULL != *position) {
        bitree_rem_left(tree, *position);
        bitree_rem_right(tree, *position);

        if (NULL != tree->destroy) {
            tree->destroy((*position)->data);
        }

        free(*position);
        *position = NULL;
        tree->size--;
    }
}

void bitree_rem_right(bitree_t *tree, bitree_node_t *node)
{
    if (0 == bitree_size(tree)) {
        return;
    }

    bitree_node_t **position = NULL;
    if (NULL == node) {
        position = &tree->root;
    } else {
        position = &node->right;
    }
    
    if (NULL != *position) {
        bitree_rem_left(tree, *position);
        bitree_rem_right(tree, *position);
    
        if (NULL != tree->destroy) {
          tree->destroy((*position)->data);
        }
    
        free(*position);
        *position = NULL;
        tree->size--;
    }
}
    
int bitree_merge(bitree_t *merge, bitree_t *left, bitree_t *right, const void
    *data)
{
    bitree_init(merge, left->destroy);
    if (0 != bitree_ins_left(merge, NULL, data)) {
        bitree_destroy(merge);
        return -1;
    }
    
    bitree_root(merge)->left = bitree_root(left);
    bitree_root(merge)->right = bitree_root(right);
    merge->size = merge->size + bitree_size(left) + bitree_size(right);
    left->root = NULL;
    left->size = 0;
    right->root = NULL;
    right->size = 0;
    return 0;
}
