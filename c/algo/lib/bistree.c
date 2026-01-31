#include <bistree.h>
#include <stdlib.h>
#include <string.h>

static void destroy_right(bistree_t *tree, bitree_node_t *node);

static void rotate_left(bitree_node_t **node)
{
    bitree_node_t *left = NULL;
    bitree_node_t *grandchild = NULL;
    
    left = bitree_left(*node);
    if (((avl_node_t *)bitree_data(left))->factor == AVL_LFT_HEAVY) {
        bitree_left(*node) = bitree_right(left);
        bitree_right(left) = *node;
        ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
        ((avl_node_t *)bitree_data(left))->factor = AVL_BALANCED;
        *node = left;
    } else {
        grandchild = bitree_right(left);
        bitree_right(left) = bitree_left(grandchild);
        bitree_left(grandchild) = left;
        bitree_left(*node) = bitree_right(grandchild);
        bitree_right(grandchild) = *node;
        
        switch (((avl_node_t *)bitree_data(grandchild))->factor) {
        case AVL_LFT_HEAVY:
            ((avl_node_t *)bitree_data(*node))->factor = AVL_RGT_HEAVY;
            ((avl_node_t *)bitree_data(left))->factor = AVL_BALANCED;
            break;
        case AVL_BALANCED:
            ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
           ((avl_node_t *)bitree_data(left))->factor = AVL_BALANCED;
           break;
        case AVL_RGT_HEAVY:
           ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
           ((avl_node_t *)bitree_data(left))->factor = AVL_LFT_HEAVY;
           break;
       }
    
       ((avl_node_t *)bitree_data(grandchild))->factor = AVL_BALANCED;
       *node = grandchild;
    }
}

static void rotate_right(bitree_node_t **node)
{
    bitree_node_t *right = NULL;
    bitree_node_t *grandchild = NULL;
    
    right = bitree_right(*node);
    if (((avl_node_t *)bitree_data(right))->factor == AVL_RGT_HEAVY) {
        bitree_right(*node) = bitree_left(right);
        bitree_left(right) = *node;
        ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
        ((avl_node_t *)bitree_data(right))->factor = AVL_BALANCED;
        *node = right;
    } else {
        grandchild = bitree_left(right);
        bitree_left(right) = bitree_right(grandchild);
        bitree_right(grandchild) = right;
        bitree_right(*node) = bitree_left(grandchild);
        bitree_left(grandchild) = *node;
        
        switch (((avl_node_t *)bitree_data(grandchild))->factor) {
        case AVL_LFT_HEAVY:
            ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
            ((avl_node_t *)bitree_data(right))->factor = AVL_RGT_HEAVY;
            break;
        case AVL_BALANCED:
            ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
            ((avl_node_t *)bitree_data(right))->factor = AVL_BALANCED;
            break;
        case AVL_RGT_HEAVY:
            ((avl_node_t *)bitree_data(*node))->factor = AVL_LFT_HEAVY;
            ((avl_node_t *)bitree_data(right))->factor = AVL_BALANCED;
            break;
        }
    
        ((avl_node_t *)bitree_data(grandchild))->factor = AVL_BALANCED;
        *node = grandchild;
    }
}

static void destroy_left(bistree_t *tree, bitree_node_t *node)
{
    bitree_node_t **position = NULL;
    
    if (0 == bitree_size(tree)) {
       return;
    }
    
    if (NULL == node) {
        position = &tree->root;
    } else {
        position = &node->left;
    }
    
    if (NULL != *position) {
        destroy_left(tree, *position);
        destroy_right(tree, *position);
    
        if (NULL != tree->destroy) {
            tree->destroy(((avl_node_t *)(*position)->data)->data);
        }
    
        free((*position)->data);
        free(*position);
        *position = NULL;
        tree->size--;
    }
}

static void destroy_right(bistree_t *tree, bitree_node_t *node)
{
    bitree_node_t **position = NULL;
    
    if (0 == bitree_size(tree)) {
        return;
    }
    
    if (NULL == node) {
       position = &tree->root;
    } else {
        position = &node->right;
    }
    
    if (NULL != *position) {
        destroy_left(tree, *position);
        destroy_right(tree, *position);
    
        if (NULL != tree->destroy) {
            tree->destroy(((avl_node_t *)(*position)->data)->data);
        }
    
        free((*position)->data);
        free(*position);
        *position = NULL;
        tree->size--;
    }
}

static int insert(bistree_t *tree, bitree_node_t **node, const void *data, int
    *balanced)
{
    avl_node_t *avl_data = NULL;
    int cmpval = 0;
    int retval = 0;
    
    if (bitree_is_eob(*node)) {
        avl_data = (avl_node_t *)malloc(sizeof(avl_node_t));
        if (NULL == avl_data) {
            return -1;
        }
    
        avl_data->factor = AVL_BALANCED;
        avl_data->hidden = 0;
        avl_data->data = (void *)data;
        return bitree_ins_left(tree, *node, avl_data);
    }

    cmpval = tree->compare(data, ((avl_node_t *)bitree_data(*node))->data);
    if (cmpval < 0) {
        if (bitree_is_eob(bitree_left(*node))) {
            avl_data = (avl_node_t *)malloc(sizeof(avl_node_t));
            if (NULL == avl_data) {
                return -1;
            }
    
            avl_data->factor = AVL_BALANCED;
            avl_data->hidden = 0;
            avl_data->data = (void *)data;
    
            if (0 != bitree_ins_left(tree, *node, avl_data)) {
                return -1;
            }
    
            *balanced = 0;
        } else {
            retval = insert(tree, &bitree_left(*node), data, balanced);
            if (0 != retval) {
                return retval;
            }
        }
    
        if (!(*balanced)) {
            switch (((avl_node_t *)bitree_data(*node))->factor) {
            case AVL_LFT_HEAVY:
                rotate_left(node);
                *balanced = 1;
                break;
            case AVL_BALANCED:
                ((avl_node_t *)bitree_data(*node))->factor = AVL_LFT_HEAVY;
                break;
            case AVL_RGT_HEAVY:
                ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
                *balanced = 1;
            }
        }
    
    } else if (cmpval > 0) {
        if (bitree_is_eob(bitree_right(*node))) {
            avl_data = (avl_node_t *)malloc(sizeof(avl_node_t));
            if (NULL == avl_data) {
                return -1;
            }

            avl_data->factor = AVL_BALANCED;
            avl_data->hidden = 0;
            avl_data->data = (void *)data;
    
            if (0 != bitree_ins_right(tree, *node, avl_data)) {
                return -1;
            }
    
            *balanced = 0;
        } else {
            retval = insert(tree, &bitree_right(*node), data, balanced);
            if (0 != retval) {
                return retval;
            }
        }
    
        if (!(*balanced)) {
            switch (((avl_node_t *)bitree_data(*node))->factor) {
            case AVL_LFT_HEAVY:
                ((avl_node_t *)bitree_data(*node))->factor = AVL_BALANCED;
                *balanced = 1;
                break;
            case AVL_BALANCED:
                ((avl_node_t *)bitree_data(*node))->factor = AVL_RGT_HEAVY;
                break;
            case AVL_RGT_HEAVY:
                rotate_right(node);
                *balanced = 1;
            }
        }
    } else {
        if (!((avl_node_t *)bitree_data(*node))->hidden) {
            return 1;
        } else {
            if (tree->destroy != NULL) {
                tree->destroy(((avl_node_t *)bitree_data(*node))->data);
            }
            
            ((avl_node_t *)bitree_data(*node))->data = (void *)data;
            ((avl_node_t *)bitree_data(*node))->hidden = 0;
            *balanced = 1;
        }
    }

    return 0;
}

static int hide(bistree_t *tree, bitree_node_t *node, const void *data)
{
    int cmpval = 0;
    int retval = 0;
    
    if (bitree_is_eob(node)) {
        return -1;
    }
    
    cmpval = tree->compare(data, ((avl_node_t *)bitree_data(node))->data);
    if (cmpval < 0) {
        retval = hide(tree, bitree_left(node), data);
    } else if (cmpval > 0) {
        retval = hide(tree, bitree_right(node), data);
    } else {
        ((avl_node_t *)bitree_data(node))->hidden = 1;
        retval = 0;
    }
    
    return retval;
}

static int lookup(bistree_t *tree, bitree_node_t *node, void **data)
{
    int cmpval = 0;
    int retval = 0;
    
    if (bitree_is_eob(node)) {
        return -1;
    }
    
    cmpval = tree->compare(*data, ((avl_node_t *)bitree_data(node))->data);
    if (cmpval < 0) {
        retval = lookup(tree, bitree_left(node), data);
    } else if (cmpval > 0) {
        retval = lookup(tree, bitree_right(node), data);
    } else {
        if (!((avl_node_t *)bitree_data(node))->hidden) {
            *data = ((avl_node_t *)bitree_data(node))->data;
            retval = 0;
        } else {
            return -1;
        }
    }
    
    return retval;
}

void bistree_init(bistree_t *tree, int (*compare)(const void *key1, const void
    *key2), void (*destroy)(void *data))
{
    bitree_init(tree, destroy);
    tree->compare = compare;
}

void bistree_destroy(bistree_t *tree)
{
    destroy_left(tree, NULL);
    memset(tree, 0, sizeof(bistree_t));
}

int bistree_insert(bistree_t *tree, const void *data) {
    int balanced = 0;

    return insert(tree, &bitree_root(tree), data, &balanced);
}

int bistree_remove(bistree_t *tree, const void *data) {
    return hide(tree, bitree_root(tree), data);
}

int bistree_lookup(bistree_t *tree, void **data) {
    return lookup(tree, bitree_root(tree), data);
}
