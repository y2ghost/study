#include <heap.h>
#include <stdio.h>
#include <stdlib.h>

static void print_heap(heap_t *heap)
{
    int i = 0;
    
    fprintf(stdout, "heap_t size is %d\n", heap_size(heap));
    for (i=0; i<heap_size(heap); i++) {
        fprintf(stdout, "Node=%03d\n", *(int *)heap->tree[i]);
    }
}

static int compare_int(const void *int1, const void *int2)
{
    
    if (*(const int *)int1 > *(const int *)int2) {
        return 1;
    } else if (*(const int *)int1 < *(const int *)int2) {
        return -1;
    }
    
    return 0;
}

int main(int argc, char **argv)
{
    heap_t heap;
    void *data = NULL;
    int intval[30] = {0};
    int i = 0;
    
    heap_init(&heap, compare_int, NULL);
    i = 0;
    intval[i] = 5;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != heap_insert(&heap, &intval[i])) {
        return 1;
    }

    print_heap(&heap);
    i++;
    intval[i] = 10;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != heap_insert(&heap, &intval[i])) {
        return 1;
    }

    print_heap(&heap);
    i++;
    intval[i] = 20;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != heap_insert(&heap, &intval[i])) {
        return 1;
    }

    print_heap(&heap);
    i++;
    intval[i] = 1;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != heap_insert(&heap, &intval[i])) {
        return 1;
    }

    print_heap(&heap);
    i++;
    intval[i] = 25;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != heap_insert(&heap, &intval[i])) {
        return 1;
    }

    print_heap(&heap);
    i++;
    intval[i] = 22;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != heap_insert(&heap, &intval[i])) {
        return 1;
    }

    print_heap(&heap);
    i++;
    intval[i] = 9;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != heap_insert(&heap, &intval[i])) {
        return 1;
    }

    print_heap(&heap);
    i++;
    
    while (heap_size(&heap) > 0) {
        if (0 != heap_extract(&heap, (void **)&data)) {
            return 1;
        }
        
        fprintf(stdout, "Extracting %03d\n", *(int *)data);
        print_heap(&heap);
    }
    
    fprintf(stdout, "Destroying the heap\n");
    heap_destroy(&heap);
    return 0;
}
