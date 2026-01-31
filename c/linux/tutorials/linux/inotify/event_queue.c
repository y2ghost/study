#include "event_queue.h"
#include <stdlib.h>
#include <stdint.h>

/* Simple queue implemented as a singly linked list with head 
and tail pointers maintained
*/
int queue_empty(queue_t q)
{
    return q->head == NULL;
}

queue_t queue_create()
{
    queue_t q = malloc(sizeof(q));

    if (NULL == q) {
        exit(-1);
    }

    q->head = NULL;
    q->tail = NULL;
    return q;
}
  
void queue_destroy(queue_t q)
{
    queue_entry_t next = NULL;

    if (NULL !=  q) {
        while(NULL != q->head) {
            next = q->head;
            q->head = next->next_ptr;
            next->next_ptr = NULL;
            free(next);
        }
        
        q->head = NULL;
        q->tail = NULL;
        free(q);
    }
}

void queue_enqueue(queue_entry_t d, queue_t q)
{
    d->next_ptr = NULL;

    if (NULL != q->tail) {
        q->tail->next_ptr = d;
        q->tail = d;
    } else {
        q->head = d;
        q->tail = d;
    }
}

queue_entry_t  queue_dequeue(queue_t q)
{
    queue_entry_t first = q->head;

    if (NULL != first) {
        q->head = first->next_ptr;
        if (NULL == q->head) {
            q->tail = NULL;
        }

        first->next_ptr = NULL;
    }

    return first;
}
