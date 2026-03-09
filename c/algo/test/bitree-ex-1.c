#include <bitree.h>
#include <stdio.h>
#include <stdlib.h>

static void print_preorder(const bitree_node_t *node)
{
    if (!bitree_is_eob(node)) {
        fprintf(stdout, "Node=%03d\n", *(int *)bitree_data(node));
        if (!bitree_is_eob(bitree_left(node))) {
            print_preorder(bitree_left(node));
        }
    
        if (!bitree_is_eob(bitree_right(node))) {
            print_preorder(bitree_right(node));
        }
    }
}

static void print_inorder(const bitree_node_t *node)
{
    if (!bitree_is_eob(node)) {
        if (!bitree_is_eob(bitree_left(node))) {
            print_inorder(bitree_left(node));
        }
    
        fprintf(stdout, "Node=%03d\n", *(int *)bitree_data(node));
        if (!bitree_is_eob(bitree_right(node))) {
            print_inorder(bitree_right(node));
        }
    }
}

static void print_postorder(const bitree_node_t *node)
{
    if (!bitree_is_eob(node)) {
        if (!bitree_is_eob(bitree_left(node))) {
            print_postorder(bitree_left(node));
        }
    
        if (!bitree_is_eob(bitree_right(node))) {
            print_postorder(bitree_right(node));
        }
    
        fprintf(stdout, "Node=%03d\n", *(int *)bitree_data(node));
    }
}

static int insert_int(bitree_t *tree, int i)
{
    bitree_node_t *node = NULL;
    bitree_node_t *prev = NULL;
    int direction = 0;
    int *data = NULL;
    
    node = tree->root;
    direction = 0;
    
    while (!bitree_is_eob(node)) {
        prev = node;
        if (i == *(int *)bitree_data(node)) {
            return -1;
        } else if (i < *(int *)bitree_data(node)) {
            node = bitree_left(node);
            direction = 1;
        } else {
            node = bitree_right(node);
            direction = 2;
        }
    }
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return -1;
    }
    
    *data = i;
    if (0 == direction) {
        return bitree_ins_left(tree, NULL, data);
    }
    
    if (1 == direction) {
        return bitree_ins_left(tree, prev, data);
    }
    
    if (2 == direction) {
        return bitree_ins_right(tree, prev, data);
    }
    
    return -1;
}

static bitree_node_t *search_int(bitree_t *tree, int i)
{
    bitree_node_t *node = NULL;
    
    node = bitree_root(tree);
    while (!bitree_is_eob(node)) {
        if (i == *(int *)bitree_data(node)) {
            return node;
        } else if (i < *(int *)bitree_data(node)) {
            node = bitree_left(node);
        } else {
            node = bitree_right(node);
        }
    }
    
    return NULL;
}

int main(int argc, char **argv)
{
    bitree_t tree;
    bitree_node_t *node = NULL;
    int i = 0;
    
    bitree_init(&tree, free);
    fprintf(stdout, "Inserting some nodes\n");
    
    if (0 != insert_int(&tree, 20)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 10)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 30)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 15)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 25)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 70)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 80)) {
        return 1;
    }

    if (0 != insert_int(&tree, 23)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 26)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 5)) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bitree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    print_preorder(bitree_root(&tree));
    i = 30;
    
    node = search_int(&tree, i);
    if (NULL == node) {
        fprintf(stdout, "Could not find %03d\n", i);
    } else {
        fprintf(stdout, "Found %03d...Removing the left tree below it\n", i);
        bitree_rem_left(&tree, node);
        fprintf(stdout, "Tree size is %d\n", bitree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        print_preorder(bitree_root(&tree));
    }
    
    i = 99;
    node = search_int(&tree, i);
    if (NULL == node) {
        fprintf(stdout, "Could not find %03d\n", i);
    } else {
        fprintf(stdout, "Found %03d...Removing the right tree below it\n", i);
        bitree_rem_right(&tree, node);
        fprintf(stdout, "Tree size is %d\n", bitree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        print_preorder(bitree_root(&tree));
    }
    
    i = 20;
    node = search_int(&tree, i);
    if (NULL == node) {
        fprintf(stdout, "Could not find %03d\n", i);
    } else {
        fprintf(stdout, "Found %03d...Removing the right tree below it\n", i);
        bitree_rem_right(&tree, node);
        fprintf(stdout, "Tree size is %d\n", bitree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        print_preorder(bitree_root(&tree));
    }
    
    i = bitree_is_leaf(bitree_root(&tree));
    fprintf(stdout, "Testing bitree_is_leaf...Value=%d (0=OK)\n", i);
    i = bitree_is_leaf(bitree_left((bitree_root(&tree))));
    fprintf(stdout, "Testing bitree_is_leaf...Value=%d (0=OK)\n", i);
    i = bitree_is_leaf(bitree_left(bitree_left((bitree_root(&tree)))));
    fprintf(stdout, "Testing bitree_is_leaf...Value=%d (1=OK)\n", i);
    i = bitree_is_leaf(bitree_right(bitree_left((bitree_root(&tree)))));
    fprintf(stdout, "Testing bitree_is_leaf...Value=%d (1=OK)\n", i);
    fprintf(stdout, "Inserting some nodes\n");
    
    if (0 != insert_int(&tree, 55)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 44)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 77)) {
        return 1;
    }
    
    if (0 != insert_int(&tree, 11)) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bitree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    print_preorder(bitree_root(&tree));
    fprintf(stdout, "(Inorder traversal)\n");
    print_inorder(bitree_root(&tree));
    fprintf(stdout, "(Postorder traversal)\n");
    print_postorder(bitree_root(&tree));
    fprintf(stdout, "Destroying the tree\n");
    bitree_destroy(&tree);
    return 0;
}
