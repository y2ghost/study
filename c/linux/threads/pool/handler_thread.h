#ifndef HANDLER_THREAD_H
#define HANDLER_THREAD_H

#include <stdio.h>
#include <pthread.h>

/* handler thread parameters structure.                      */
/* this is used to pass a thread several parameters,         */
/* even thought a thread's function gets only one parameter. */
struct handler_thread_params {
    int thread_id;
    pthread_mutex_t* request_mutex;
    pthread_cond_t*  got_request;
    struct requests_queue* requests;
};

/* a handler thread's main loop function */
extern void* handle_requests_loop(void* thread_params);

#endif /* HANDLER_THREAD_H */
