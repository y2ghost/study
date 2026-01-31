#ifndef QUEUE_H
#define QUEUE_H

#include <list.h>

typedef list_t queue_t;

#define queue_init list_init
#define queue_destroy list_destroy

int queue_enqueue(queue_t *queue, const void *data);
int queue_dequeue(queue_t *queue, void **data);

#define queue_peek(queue)   (0==(queue)->head ? 0 : (queue)->head->data)
#define queue_size list_size

#endif /* QUEUE_H */
