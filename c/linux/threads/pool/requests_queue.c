#include "requests_queue.h"

#include <stdlib.h>
#include <assert.h>

/*
 * function init_requests_queue(): create a requests queue.
 * algorithm: creates a request queue structure, initialize with given
 *            parameters.
 * input:     queue's mutex, queue's condition variable.
 * output:    none.
 */
struct requests_queue*
init_requests_queue(pthread_mutex_t* p_mutex, pthread_cond_t*  p_cond_var)
{
    struct requests_queue* queue = (struct requests_queue*)malloc(sizeof(*queue));

    if (NULL == queue) {
        fprintf(stderr, "out of memory. exiting\n");
        exit(1);
    }

    /* initialize queue */
    queue->requests = NULL;
    queue->last_request = NULL;
    queue->num_requests = 0;
    queue->p_mutex = p_mutex;
    queue->p_cond_var = p_cond_var;
    return queue;
}

/*
 * function add_request(): add a request to the requests list
 * algorithm: creates a request structure, adds to the list, and
 *            increases number of pending requests by one.
 * input:     pointer to queue, request number.
 * output:    none.
 */
void add_request(struct requests_queue* queue, int request_num)
{
    struct request* a_request = NULL;

    /* sanity check - amke sure queue is not NULL */
    assert(queue);
    /* create structure with new request */
    a_request = (struct request*)malloc(sizeof(*a_request));

    if (NULL == a_request) {
        fprintf(stderr, "add_request: out of memory\n");
        exit(1);
    }

    a_request->number = request_num;
    a_request->next = NULL;
    /* lock the mutex, to assure exclusive access to the list */
    pthread_mutex_lock(queue->p_mutex);

    /* add new request to the end of the list, updating list */
    /* pointers as required */
    if (0 == queue->num_requests) {
        queue->requests = a_request;
        queue->last_request = a_request;
    } else {
        queue->last_request->next = a_request;
        queue->last_request = a_request;
    }

    /* increase total number of pending requests by one. */
    queue->num_requests++;
    /* unlock mutex */
    pthread_mutex_unlock(queue->p_mutex);
    /* signal the condition variable - there's a new request to handle */
    pthread_cond_signal(queue->p_cond_var);
}

/*
 * function get_request(): gets the first pending request from the requests list
 *                         removing it from the list.
 * algorithm: creates a request structure, adds to the list, and
 *            increases number of pending requests by one.
 * input:     pointer to requests queue.
 * output:    pointer to the removed request, or NULL if none.
 * memory:    the returned request need to be freed by the caller.
 */
struct request* get_request(struct requests_queue* queue)
{
    struct request* a_request = NULL;

    /* sanity check - amke sure queue is not NULL */
    assert(queue);
    /* lock the mutex, to assure exclusive access to the list */
    pthread_mutex_lock(queue->p_mutex);

    if (queue->num_requests > 0) {
        a_request = queue->requests;
        queue->requests = a_request->next;
        
        if (queue->requests == NULL) {
            queue->last_request = NULL;
        }
        
        /* decrease the total number of pending requests */
        queue->num_requests--;
    } else {
        a_request = NULL;
    }

    /* unlock mutex */
    pthread_mutex_unlock(queue->p_mutex);
    /* return the request to the caller. */
    return a_request;
}

/*
 * function get_requests_number(): get the number of requests in the list.
 * input:     pointer to requests queue.
 * output:    number of pending requests on the queue.
 */
int get_requests_number(struct requests_queue* queue)
{
    int num_requests = 0;

    /* sanity check */ 
    assert(queue);
    /* lock the mutex, to assure exclusive access to the list */
    pthread_mutex_lock(queue->p_mutex);
    num_requests = queue->num_requests;
    /* unlock mutex */
    pthread_mutex_unlock(queue->p_mutex);
    return num_requests;
}

/*
 * function delete_requests_queue(): delete a requests queue.
 * algorithm: delete a request queue structure, and free all memory it uses.
 * input:     pointer to requests queue.
 * output:    none.
 */
void delete_requests_queue(struct requests_queue* queue)
{
    struct request* a_request = NULL;

    /* sanity check - amke sure queue is not NULL */
    /* first free any requests that might be on the queue */
    assert(queue);
    while (queue->num_requests > 0) {
        a_request = get_request(queue);
        free(a_request);
    }

    /* finally, free the queue's struct itself */
    free(queue);
}
