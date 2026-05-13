#include <bistree.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define STRSIZ  6

static void preorder_tree(const bitree_node_t *node)
{
    if (!bitree_is_eob(node)) {
        avl_node_t *avl_node = bitree_data(node);
        
        fprintf(stdout, "Node=%s, %+2d, hidden=%d\n",
            (char*)avl_node->data, avl_node->factor, avl_node->hidden);
        if (!bitree_is_eob(bitree_left(node))) {
            preorder_tree(bitree_left(node));
        }
       
        if (!bitree_is_eob(bitree_right(node))) {
            preorder_tree(bitree_right(node));
        }
    }
}

static int compare_str(const void *str1, const void *str2)
{
    int retval = 0;
    
    retval = strcmp((const char *)str1, (const char *)str2);
    if (retval > 0) {
        return 1;
    } else if (retval < 0) {
        return -1;
    }
    
    return 0;
}

typedef enum word_t_ {
    HOP, HAT, TAP, BAT,
    TIP, MOP, MOM, CAT,
    ZOO, WAX, TOP, DIP,
} word_t;

int main(int argc, char **argv)
{
    bitree_t tree;
    char *target = NULL;
    char sarray[12][STRSIZ] = {{'\0'}};
    char tarray[12][STRSIZ] = {{'\0'}};
    
    strcpy(sarray[HOP], "HOP");
    strcpy(sarray[HAT], "HAT");
    strcpy(sarray[TAP], "TAP");
    strcpy(sarray[BAT], "BAT");
    strcpy(sarray[TIP], "TIP");
    strcpy(sarray[MOP], "MOP");
    strcpy(sarray[MOM], "MOM");
    strcpy(sarray[CAT], "CAT");
    strcpy(sarray[ZOO], "ZOO");
    strcpy(sarray[WAX], "WAX");
    strcpy(sarray[TOP], "TOP");
    strcpy(sarray[DIP], "DIP");
    bistree_init(&tree, compare_str, NULL);
    fprintf(stdout, "Inserting some nodes\n");
    
    if (0 != bistree_insert(&tree, sarray[TAP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[TIP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[TOP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[CAT])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[BAT])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    fprintf(stdout, "Removing %s\n", sarray[TAP]);
    
    if (0 != bistree_remove(&tree, &sarray[TAP])) {
        fprintf(stdout, "Could not find %s\n", sarray[TAP]);
    } else {
        fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        preorder_tree(bitree_root(&tree));
    }
    
    fprintf(stdout, "Removing %s\n", sarray[TOP]);
    if (0 != bistree_remove(&tree, &sarray[TOP])) {
        fprintf(stdout, "Could not find %s\n", sarray[TOP]);
    } else {
        fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        preorder_tree(bitree_root(&tree));
    }
    
    fprintf(stdout, "Removing %s\n", sarray[TIP]);
    if (0 != bistree_remove(&tree, &sarray[TIP])) {
        fprintf(stdout, "Could not find %s\n", sarray[TIP]);
    } else {
        fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        preorder_tree(bitree_root(&tree));
    }
    
    fprintf(stdout, "Removing %s\n", sarray[HOP]);
    if (0 != bistree_remove(&tree, &sarray[HOP])) {
        fprintf(stdout, "Could not find %s\n", sarray[HOP]);
    } else {
        fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        preorder_tree(bitree_root(&tree));
    }
    
    fprintf(stdout, "Inserting %s\n", sarray[HOP]);
    if (0 != bistree_insert(&tree, sarray[HOP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    fprintf(stdout, "Inserting %s\n", sarray[DIP]);
    
    if (0 != bistree_insert(&tree, sarray[DIP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    fprintf(stdout, "Inserting %s\n", sarray[TAP]);

    if (0 != bistree_insert(&tree, sarray[TAP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    fprintf(stdout, "Inserting %s\n", sarray[TOP]);
    
    if (0 != bistree_insert(&tree, sarray[TOP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    fprintf(stdout, "Inserting %s\n", sarray[TIP]);
    
    if (0 != bistree_insert(&tree, sarray[TIP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    fprintf(stdout, "Inserting more nodes\n");
    
    if (0 != bistree_insert(&tree, sarray[MOM])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[HAT])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[MOP])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[WAX])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    
    if (0 != bistree_insert(&tree, sarray[ZOO])) {
        return 1;
    }
    
    fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
    fprintf(stdout, "(Preorder traversal)\n");
    preorder_tree(bitree_root(&tree));
    fprintf(stdout, "Removing %s\n", sarray[WAX]);
    
    if (0 != bistree_remove(&tree, &sarray[WAX])) {
        fprintf(stdout, "Could not find %s\n", sarray[WAX]);
    } else {
        fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        preorder_tree(bitree_root(&tree));
    }
    
    fprintf(stdout, "Removing %s\n", sarray[HOP]);
    if (0 != bistree_remove(&tree, &sarray[HOP])) {
        fprintf(stdout, "Could not find %s\n", sarray[HOP]);
    } else {
        fprintf(stdout, "Tree size is %d\n", bistree_size(&tree));
        fprintf(stdout, "(Preorder traversal)\n");
        preorder_tree(bitree_root(&tree));
    }
    
    fprintf(stdout, "Looking up some nodes\n");
    strcpy(tarray[0], "TOP");
    strcpy(tarray[1], "HOP");
    strcpy(tarray[2], "WAX");
    strcpy(tarray[3], "HAT");
    strcpy(tarray[4], "XXX");
    target = tarray[0];
    
    if (-1 == bistree_lookup(&tree, (void **)&target)) {
        fprintf(stdout, "Could not find %s\n", target);
    } else {
        fprintf(stdout, "Found %s\n", target);
    }
    
    target = tarray[1];
    if (-1 == bistree_lookup(&tree, (void **)&target)) {
        fprintf(stdout, "Could not find %s\n", target);
    } else {
        fprintf(stdout, "Found %s\n", target);
    }
    
    target = tarray[2];
    if (-1 == bistree_lookup(&tree, (void **)&target)) {
        fprintf(stdout, "Could not find %s\n", target);
    } else {
        fprintf(stdout, "Found %s\n", target);
    }
    
    target = tarray[3];
    if (-1 == bistree_lookup(&tree, (void **)&target)) {
        fprintf(stdout, "Could not find %s\n", target);
    } else {
        fprintf(stdout, "Found %s\n", target);
    }
    
    target = tarray[4];
    if (-1 == bistree_lookup(&tree, (void **)&target)) {
        fprintf(stdout, "Could not find %s\n", target);
    } else {
        fprintf(stdout, "Found %s\n", target);
    }
    
    fprintf(stdout, "Destroying the search tree\n");
    bistree_destroy(&tree);
    return 0;
}
