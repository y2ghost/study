#include "requests_queue.h"
#include "handler_thread.h"

#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <assert.h>

extern int done_creating_requests;

/*
 * function cleanup_free_mutex(): free the mutex, if it's locked.
 * input:     pointer to a mutex structure.
 * output:    none.
 */
static void cleanup_free_mutex(void* a_mutex)
{
    pthread_mutex_t* p_mutex = (pthread_mutex_t*)a_mutex;

    if (p_mutex) {
        pthread_mutex_unlock(p_mutex);
    }
}

/*
 * function handle_request(): handle a single given request.
 * algorithm: prints a message stating that the given thread handled
 *            the given request.
 * input:     request pointer, id of calling thread.
 * output:    none.
 */
static void handle_request(struct request* a_request, int thread_id)
{
    if (NULL != a_request) {
        int i = 0;
        
        for (i = 0; i<100000; i++) {}
    }
}

/*
 * function handle_requests_loop(): infinite loop of requests handling
 * algorithm: forever, if there are requests to handle, take the first
 *            and handle it. Then wait on the given condition variable,
 *            and when it is signaled, re-do the loop.
 *            increases number of pending requests by one.
 * input:     id of thread, for printing purposes.
 * output:    none.
 */
void* handle_requests_loop(void* thread_params)
{
    struct request* a_request = NULL;
    struct handler_thread_params *data = NULL;

    /* sanity check -make sure data isn't NULL */
    data = (struct handler_thread_params*)thread_params;
    assert(data);
    printf("Starting thread '%d'\n", data->thread_id);
    fflush(stdout);
    /* set my cancel state to 'enabled', and cancel type to 'defered'. */
    pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
    pthread_setcanceltype(PTHREAD_CANCEL_DEFERRED, NULL);
    /* set thread cleanup handler */
    pthread_cleanup_push(cleanup_free_mutex, (void*)data->request_mutex);
    /* lock the mutex, to access the requests list exclusively. */
    pthread_mutex_lock(data->request_mutex);

    /* do forever.... */
    while (1) {
        int num_requests = get_requests_number(data->requests);

        if (num_requests > 0) {
            a_request = get_request(data->requests);
            if (NULL != a_request) {
                /* unlock mutex - so other threads would be able to handle */
                /* other reqeusts waiting in the queue paralelly.          */
                pthread_mutex_unlock(data->request_mutex);
                handle_request(a_request, data->thread_id);
                free(a_request);
                /* and lock the mutex again. */
                pthread_mutex_lock(data->request_mutex);
            }
        } else {
            /* the thread checks the flag before waiting            */
            /* on the condition variable.                           */
            /* if no new requests are going to be generated, exit.  */
            if (0 != done_creating_requests) {
                pthread_mutex_unlock(data->request_mutex);
                printf("thread '%d' exiting\n", data->thread_id);
                fflush(stdout);
                pthread_exit(NULL);
            }
            
            /* wait for a request to arrive. note the mutex will be */
            /* unlocked here, thus allowing other threads access to */
            /* requests list.                                       */
            pthread_cond_wait(data->got_request, data->request_mutex);
            /* and after we return from pthread_cond_wait, the mutex  */
            /* is locked again, so we don't need to lock it ourselves */
        }
    }

    /* remove thread cleanup handler. never reached, but we must use */
    /* it here, according to pthread_cleanup_push's manual page.     */
    pthread_cleanup_pop(0);
}
