#ifndef HANDLER_THREADS_QUEUE_H
#define HANDLER_THREADS_QUEUE_H

#include "requests_queue.h"
#include "handler_thread.h"

#include <stdio.h>
#include <pthread.h>

struct handler_thread {
    pthread_t thread;
    int thr_id;
    struct handler_thread* next;
};

struct handler_threads_pool {
    struct handler_thread* threads;
    struct handler_thread* last_thread;
    int num_threads;
    int max_thr_id;
    pthread_mutex_t* p_mutex;
    pthread_cond_t*  p_cond_var;
    struct requests_queue* requests;
};

/*
 * create a handler threads pool. associate it with the given mutex
 * and condition variables.
 */
extern struct handler_threads_pool*
init_handler_threads_pool(pthread_mutex_t* p_mutex,
    pthread_cond_t*  p_cond_var,
    struct requests_queue* requests);

/* spawn a new handler thread and add it to the threads pool. */
extern void add_handler_thread(struct handler_threads_pool* pool);

/* delete the first thread from the threads pool (and cancel the thread) */
extern void delete_handler_thread(struct handler_threads_pool* pool);

/* get the number of handler threads currently in the threads pool */
extern int get_handler_threads_number(struct handler_threads_pool* pool);

/*
 * free the resources taken by the given requests queue,
 * and cancel all its threads.
 */
extern void delete_handler_threads_pool(struct handler_threads_pool* pool);

#endif /* HANDLER_THREADS_QUEUE_H */
