#ifndef EVENT_QUEUE_H_
#define EVENT_QUEUE_H_

#include <stdint.h>
#include <sys/inotify.h>

typedef struct queue_entry *queue_entry_t;
typedef struct queue_struct *queue_t;

struct queue_entry {
    struct queue_entry *next_ptr;
    struct inotify_event inot_ev;
};

struct queue_struct {
    struct queue_entry *head;
    struct queue_entry *tail;
};

int queue_empty(queue_t q);
queue_t queue_create();
void queue_destroy(queue_t q);
void queue_enqueue(queue_entry_t d, queue_t q);
queue_entry_t queue_dequeue(queue_t q);

#endif /* EVENT_QUEUE_H_ */
