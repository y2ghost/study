#include <pqueue.h>
#include <stdio.h>
#include <stdlib.h>

static void print_pqueue(pqueue_t *pqueue)
{
    int i = 0;
    
    fprintf(stdout, "Priority queue size is %d\n", pqueue_size(pqueue));
    for (i=0; i<pqueue_size(pqueue); i++) {
        fprintf(stdout, "Node=%03d\n", *(int *)pqueue->tree[i]);
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
    pqueue_t pqueue;
    void *data = NULL;
    int intval[30] = {0};
    int i = 0;
    
    pqueue_init(&pqueue, compare_int, NULL);
    i = 0;
    intval[i] = 5;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != pqueue_insert(&pqueue, &intval[i])) {
        return 1;
    }

    print_pqueue(&pqueue);
    i++;
    intval[i] = 10;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != pqueue_insert(&pqueue, &intval[i])) {
        return 1;
    }

    print_pqueue(&pqueue);
    i++;
    intval[i] = 20;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != pqueue_insert(&pqueue, &intval[i])) {
        return 1;
    }

    print_pqueue(&pqueue);
    i++;
    intval[i] = 1;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != pqueue_insert(&pqueue, &intval[i])) {
        return 1;
    }

    print_pqueue(&pqueue);
    i++;
    intval[i] = 25;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != pqueue_insert(&pqueue, &intval[i])) {
        return 1;
    }

    print_pqueue(&pqueue);
    i++;
    intval[i] = 22;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != pqueue_insert(&pqueue, &intval[i])) {
        return 1;
    }

    print_pqueue(&pqueue);
    i++;
    intval[i] = 12;
    fprintf(stdout, "Inserting %03d\n", intval[i]);

    if (0 != pqueue_insert(&pqueue, &intval[i])) {
        return 1;
    }

    print_pqueue(&pqueue);
    i++;
    
    while (pqueue_size(&pqueue) > 0) {
        fprintf(stdout, "Peeking at the highest priority element..Value=%03d\n",
            *(int *)pqueue_peek(&pqueue));
        if (0 != pqueue_extract(&pqueue, (void **)&data)) {
            return 1;
        }

        fprintf(stdout, "Extracting %03d\n", *(int *)data);
        print_pqueue(&pqueue);
    }
    
    fprintf(stdout, "Destroying the pqueue\n");
    pqueue_destroy(&pqueue);
    return 0;
}
