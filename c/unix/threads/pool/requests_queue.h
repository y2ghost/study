#ifndef REQUESTS_QUEUE_H
#define REQUESTS_QUEUE_H

#include <stdio.h>
#include <pthread.h>

struct request {
    int number;
    struct request* next;
};

struct requests_queue {
    struct request* requests;
    struct request* last_request;
    int num_requests;
    pthread_mutex_t* p_mutex;
    pthread_cond_t*  p_cond_var;
};

/*
 * create a requests queue. associate it with the given mutex
 * and condition variables.
 */
extern struct requests_queue* init_requests_queue(pthread_mutex_t* p_mutex, pthread_cond_t*  p_cond_var);

/* add a request to the requests list */
extern void add_request(struct requests_queue* queue, int request_num);

/* get the first pending request from the requests list */
extern struct request* get_request(struct requests_queue* queue);

/* get the number of requests in the list */
extern int get_requests_number(struct requests_queue* queue);

/* free the resources taken by the given requests queue */
extern void delete_requests_queue(struct requests_queue* queue);

#endif /* REQUESTS_QUEUE_H */
