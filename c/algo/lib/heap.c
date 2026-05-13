#include <heap.h>
#include <stdlib.h>
#include <string.h>

#define heap_parent(npos)   ((int)(((npos) - 1) / 2))
#define heap_left(npos)     (((npos) * 2) + 1)
#define heap_right(npos)    (((npos) * 2) + 2)

void heap_init(heap_t *heap,
    int (*compare)(const void *key1, const void *key2),
    void (*destroy)(void *data))
{
    heap->size = 0;
    heap->compare = compare;
    heap->destroy = destroy;
    heap->tree = NULL;
}

void heap_destroy(heap_t *heap)
{
    if (NULL != heap->destroy) {
        for (int i=0; i<heap_size(heap); i++) {
            heap->destroy(heap->tree[i]);
        }
    }
    
    free(heap->tree);
    memset(heap, 0, sizeof(heap_t));
}

int heap_insert(heap_t *heap, const void *data)
{
    void *temp = (void **)realloc(heap->tree, (heap_size(heap) + 1)*sizeof(void*));
    if (NULL == temp) {
        return -1;
    } else {
        heap->tree = temp;
    }
    
    heap->tree[heap_size(heap)] = (void *)data;
    int ipos = heap_size(heap);
    int ppos = heap_parent(ipos);
    
    while (ipos > 0 && heap->compare(heap->tree[ppos], heap->tree[ipos]) < 0) {
        temp = heap->tree[ppos];
        heap->tree[ppos] = heap->tree[ipos];
        heap->tree[ipos] = temp;
        ipos = ppos;
        ppos = heap_parent(ipos);
    }
    
    heap->size++;
    return 0;
}

int heap_extract(heap_t *heap, void **data)
{
    void *save = NULL;
    void *temp = NULL;
    int ipos = 0;
    int lpos = 0;
    int rpos = 0;
    int mpos = 0;
    
    if (0 == heap_size(heap)) {
        return -1;
    }
    
    *data = heap->tree[0];
    save = heap->tree[heap_size(heap) - 1];
    
    if (heap_size(heap) - 1 > 0) {
        temp = (void**)realloc(heap->tree, (heap_size(heap)-1) * sizeof(void*));
        if (NULL ==temp) {
            return -1;
        } else {
            heap->tree = temp;
        }
    
        heap->size--;
    } else {
        free(heap->tree);
        heap->tree = NULL;
        heap->size = 0;
        return 0;
    }
    
    heap->tree[0] = save;
    ipos = 0;
    lpos = heap_left(ipos);
    rpos = heap_right(ipos);
    
    while (1) {
        lpos = heap_left(ipos);
        rpos = heap_right(ipos);
    
        if (lpos<heap_size(heap) && heap->compare(heap->tree[lpos], heap->tree[ipos])>0) {
            mpos = lpos;
        } else {
            mpos = ipos;
        }
    
        if (rpos<heap_size(heap) && heap->compare(heap->tree[rpos], heap->tree[mpos])>0) {
            mpos = rpos;
        }
    
        if (mpos == ipos) {
            break;
        } else {
            temp = heap->tree[mpos];
            heap->tree[mpos] = heap->tree[ipos];
            heap->tree[ipos] = temp;
            ipos = mpos;
        }
    }

    return 0;
}
