#include <list.h>
#include <queue.h>
#include <stdio.h>
#include <stdlib.h>

static void print_queue(const queue_t *queue)
{
    list_elm_t *element = NULL;
    int *data = NULL;
    int size = 0;
    int i = 0;
    
    fprintf(stdout, "queue_t size is %d\n", size = queue_size(queue));
    i = 0;
    element = list_head(queue);
    
    while (i < size) {
        data = list_data(element);
        fprintf(stdout, "queue[%03d]=%03d\n", i, *data);
        element = list_next(element);
        i++;
    }
}

int main(int argc, char **argv)
{
    queue_t queue;
    int *data = NULL;
    int i = 0;
    
    queue_init(&queue, free);
    fprintf(stdout, "Enqueuing 10 elements\n");
    
    for (i=0; i<10; i++) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }
    
        *data = i + 1;
        if (0 != queue_enqueue(&queue, data)) {
            return 1;
        }
    }
    
    print_queue(&queue);
    fprintf(stdout, "Dequeuing 5 elements\n");
    
    for (i=0; i<5; i++) {
        if (0 == queue_dequeue(&queue, (void **)&data)) {
            free(data);
        } else {
            return 1;
        }
    }
    
    print_queue(&queue);
    fprintf(stdout, "Enqueuing 100 and 200\n");
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 100;
    if (0 != queue_enqueue(&queue, data)) {
        return 1;
    }
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 200;
    if (0 != queue_enqueue(&queue, data)) {
        return 1;
    }
    
    print_queue(&queue);
    data = queue_peek(&queue);

    if (NULL != data) {
       fprintf(stdout, "Peeking at the head element...Value=%03d\n", *data);
    } else {
        fprintf(stdout, "Peeking at the head element...Value=NULL\n");
    }
    
    print_queue(&queue);
    fprintf(stdout, "Dequeuing all elements\n");
    
    while (queue_size(&queue) > 0) {
        if (0 == queue_dequeue(&queue, (void **)&data)) {
            free(data);
        }
    }
    
    data = queue_peek(&queue);
    if (NULL != data) {
        fprintf(stdout, "Peeking at an empty queue...Value=%03d\n", *data);
    } else {
        fprintf(stdout, "Peeking at an empty queue...Value=NULL\n");
    }
    
    fprintf(stdout, "Destroying the queue\n");
    queue_destroy(&queue);
    return 0;
}
