#include <bit.h>
#include <bitree.h>
#include <compress.h>
#include <pqueue.h>
#include <limits.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>

static int compare_freq(const void *tree1, const void *tree2)
{
    huff_node_t *root1 = NULL;
    huff_node_t *root2 = NULL;
    
    root1 = (huff_node_t *)bitree_data(bitree_root((const bitree_t *)tree1));
    root2 = (huff_node_t *)bitree_data(bitree_root((const bitree_t *)tree2));
    
    if (root1->freq < root2->freq) {
        return 1;
    } else if (root1->freq > root2->freq) {
        return -1;
    }
    
    return 0;
}

static void destroy_tree(void *tree)
{
    bitree_destroy(tree);
    free(tree);
}

static int build_tree(int *freqs, bitree_t **tree)
{
    bitree_t *init = NULL;
    bitree_t *merge = NULL;
    bitree_t *left = NULL;
    bitree_t *right = NULL;
    pqueue_t pqueue;
    huff_node_t *data = NULL;
    int size = 0;
    int c = 0;
    
    *tree = NULL;
    pqueue_init(&pqueue, compare_freq, destroy_tree);
    
    for (c=0; c<=UCHAR_MAX; c++) {
        if (0 != freqs[c]) {
            init = (bitree_t *)malloc(sizeof(bitree_t));
            if (NULL == init) {
                pqueue_destroy(&pqueue);
                return -1;
            }
    
            bitree_init(init, free);
            data = (huff_node_t *)malloc(sizeof(huff_node_t));
            if (NULL == data) {
                pqueue_destroy(&pqueue);
                return -1;
            }
    
            data->symbol = c;
            data->freq = freqs[c];
    
            if (0 != bitree_ins_left(init, NULL, data)) {
                free(data);
                bitree_destroy(init);
                free(init);
                pqueue_destroy(&pqueue);
                return -1;
            }
    
            if (0 != pqueue_insert(&pqueue, init)) {
                bitree_destroy(init);
                free(init);
                pqueue_destroy(&pqueue);
                return -1;
            }
        }
    }
    
    size = pqueue_size(&pqueue);
    for (c=1; c<=size-1; c++) {
        merge = (bitree_t *)malloc(sizeof(bitree_t));
        if (NULL == merge) {
            pqueue_destroy(&pqueue);
            return -1;
        }
    
        if (0 != pqueue_extract(&pqueue, (void **)&left)) {
            pqueue_destroy(&pqueue);
            free(merge);
            return -1;
        }
    
        if (0 != pqueue_extract(&pqueue, (void **)&right)) {
            pqueue_destroy(&pqueue);
            free(merge);
            return -1;
        }
    
        data = (huff_node_t *)malloc(sizeof(huff_node_t));
        if (NULL == data) {
            pqueue_destroy(&pqueue);
            free(merge);
            return -1;
        }

        memset(data, 0, sizeof(huff_node_t));
        data->freq = ((huff_node_t *)bitree_data(bitree_root(left)))->freq +
            ((huff_node_t *)bitree_data(bitree_root(right)))->freq;
    
        if (0 != bitree_merge(merge, left, right, data)) {
            pqueue_destroy(&pqueue);
            free(merge);
            return -1;
        }
    
        if (0 != pqueue_insert(&pqueue, merge)) {
            pqueue_destroy(&pqueue);
            bitree_destroy(merge);
            free(merge);
            return -1;
        }
    
        free(left);
        free(right);
    }
    
    if (0 != pqueue_extract(&pqueue, (void **)tree)) {
        pqueue_destroy(&pqueue);
        return -1;
    } else {
        pqueue_destroy(&pqueue);
    }
    
    return 0;
}

static void build_table(bitree_node_t *node, unsigned short code,
    unsigned char size, huff_code_t *table)
{
    if (!bitree_is_eob(node)) {
        if (!bitree_is_eob(bitree_left(node))) {
            build_table(bitree_left(node), code << 1, size + 1, table);
        }
    
        if (!bitree_is_eob(bitree_right(node))) {
            build_table(bitree_right(node), (code << 1) | 0x0001, size + 1, table);
        }
    
        if (bitree_is_eob(bitree_left(node))&&bitree_is_eob(bitree_right(node))) {
            code = htons(code);
            table[((huff_node_t *)bitree_data(node))->symbol].used = 1;
            table[((huff_node_t *)bitree_data(node))->symbol].code = code;
            table[((huff_node_t *)bitree_data(node))->symbol].size = size;
        }
    }
}

int huffman_compress(const unsigned char *original,
    unsigned char **compressed, int size)
{
    bitree_t *tree = NULL;
    huff_code_t table[UCHAR_MAX + 1];
    int freqs[UCHAR_MAX + 1] = {0};
    int max = 0;
    int scale = 0;
    int hsize = 0;
    int ipos = 0;
    int opos = 0;
    int cpos = 0;
    int c = 0;
    int i = 0;
    unsigned char *comp = NULL;
    unsigned char *temp = NULL;
    
    *compressed = NULL;
    for (c=0; c<=UCHAR_MAX; c++) {
        freqs[c] = 0;
    }
    
    ipos = 0;
    if (size > 0) {
        while (ipos < size) {
            freqs[original[ipos]]++;
            ipos++;
        }
    }
    
    max = UCHAR_MAX;
    for (c=0; c<=UCHAR_MAX; c++) {
        if (freqs[c] > max) {
            max = freqs[c];
        }
    }
    
    for (c=0; c<=UCHAR_MAX; c++) {
        scale = (int)(freqs[c] / ((double)max / (double)UCHAR_MAX));
        if (0==scale && 0!=freqs[c]) {
            freqs[c] = 1;
        } else {
            freqs[c] = scale;
        }
    }
    
    if (0 != build_tree(freqs, &tree)) {
        return -1;
    }
    
    for (c=0; c<=UCHAR_MAX; c++) {
        memset(&table[c], 0, sizeof(huff_code_t));
    }
    
    build_table(bitree_root(tree), 0x0000, 0, table);
    bitree_destroy(tree);
    free(tree);
    hsize = sizeof(int) + (UCHAR_MAX + 1);
    comp = (unsigned char *)malloc(hsize);

    if (NULL == comp) {
        return -1;
    }
    
    memcpy(comp, &size, sizeof(int));
    for (c=0; c<=UCHAR_MAX; c++) {
        comp[sizeof(int) + c] = (unsigned char)freqs[c];
    }
    
    ipos = 0;
    opos = hsize * 8;
    
    while (ipos < size) {
        c = original[ipos];
        for (i=0; i<table[c].size; i++) {
            if (opos % 8 == 0) {
                temp = (unsigned char *)realloc(comp,(opos / 8) + 1);
                if (NULL == temp) {
                    free(comp);
                    return -1;
                }

                comp = temp;
            }
    
            cpos = (sizeof(short) * 8) - table[c].size + i;
            bit_set(comp, opos, bit_get((unsigned char *)&table[c].code, cpos));
            opos++;
        }
        
        ipos++;
    }
    
    *compressed = comp;
    return ((opos - 1) / 8) + 1;
}

int huffman_uncompress(const unsigned char *compressed,
    unsigned char **original)
{
    bitree_t *tree = NULL;
    bitree_node_t *node = NULL;
    int freqs[UCHAR_MAX + 1] = {0};
    int hsize = 0;
    int size = 0;
    int ipos = 0;
    int opos = 0;
    int state = 0;
    int c = 0;
    unsigned char *orig = NULL;
    unsigned char *temp = NULL;
    
    *original = NULL;
    orig = NULL;
    hsize = sizeof(int) + (UCHAR_MAX + 1);
    memcpy(&size, compressed, sizeof(int));
    
    for (c=0; c<=UCHAR_MAX; c++) {
        freqs[c] = compressed[sizeof(int) + c];
    }
    
    if (0 != build_tree(freqs, &tree)) {
        return -1;
    }
    
    ipos = hsize * 8;
    opos = 0;
    node = bitree_root(tree);
    
    while (opos < size) {
        state = bit_get(compressed, ipos);
        ipos++;
    
        if (0 == state) {
            if (bitree_is_eob(node) || bitree_is_eob(bitree_left(node))) {
                bitree_destroy(tree);
                free(tree);
                return -1;
            } else {
                node = bitree_left(node);
            }
        } else {
            if (bitree_is_eob(node) || bitree_is_eob(bitree_right(node))) {
                bitree_destroy(tree);
                free(tree);
                return -1;
            } else {
                node = bitree_right(node);
            }
        }
    
        if (bitree_is_eob(bitree_left(node))&&bitree_is_eob(bitree_right(node))) {
            if (opos > 0) {
                temp = (unsigned char *)realloc(orig, opos + 1);
                if (NULL == temp) {
                    bitree_destroy(tree);
                    free(tree);
                    free(orig);
                    return -1;
                }
    
                orig = temp;
            } else {
                orig = (unsigned char *)malloc(1);
                if (NULL == orig) {
                    bitree_destroy(tree);
                    free(tree);
                    return -1;
                }
            }
    
            orig[opos] = ((huff_node_t *)bitree_data(node))->symbol;
            opos++;
            node = bitree_root(tree);
        }
    }
    
    bitree_destroy(tree);
    free(tree);
    *original = orig;
    return opos;
}
