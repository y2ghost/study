#include "workq.h"
#include "errors.h"
#include <pthread.h>
#include <stdlib.h>
#include <time.h>

static void *workq_server(void *arg)
{
    int status = 0;
    int timedout = 0;
    struct timespec timeout;
    workq_ele_t *we = NULL;
    workq_t *wq = (workq_t*)arg;

    DPRINTF(("A worker is starting\n"));
    status = pthread_mutex_lock(&wq->mutex);

    if (0 != status) {
        return NULL;
    }

    while (1) {
        timedout = 0;
        DPRINTF(("Worker waiting for work\n"));
        clock_gettime(CLOCK_REALTIME, &timeout);
        timeout.tv_sec += 2;

        while (NULL==wq->first && 0==wq->quit) {
            status = pthread_cond_timedwait(&wq->cv, &wq->mutex, &timeout);
            if (ETIMEDOUT == status) {
                DPRINTF(("Worker wait timed out\n"));
                timedout = 1;
                break;
            } else if (0 != status) {
                DPRINTF(("Worker wait failed, %d(%s)\n",
                    status, strerror(status)));
                wq->counter--;
                pthread_mutex_unlock(&wq->mutex);
                return NULL;
            }
        }

        DPRINTF(("Work queue: %p, quit: %d\n", wq->first, wq->quit));
        we = wq->first;

        if (NULL != we) {
            wq->first = we->next;
            if (we == wq->last) {
                wq->last = NULL;
            }

            status = pthread_mutex_unlock(&wq->mutex);
            if (0 != status) {
                return NULL;
            }

            DPRINTF(("Worker calling engine\n"));
            wq->engine(we->data);
            free(we);
            status = pthread_mutex_lock(&wq->mutex);

            if (0 != status) {
                return NULL;
            }
        }

        if (NULL==wq->first && 0!=wq->quit) {
            DPRINTF(("Worker shutting down\n"));
            wq->counter--;

            if (0 == wq->counter) {
                pthread_cond_broadcast(&wq->cv);
            }

            pthread_mutex_unlock(&wq->mutex);
            return NULL;
        }

        if (NULL==wq->first && 0!=timedout) {
            DPRINTF(("engine terminating due to timeout.\n"));
            wq->counter--;
            break;
        }
    }

    pthread_mutex_unlock(&wq->mutex);
    DPRINTF(("Worker exiting\n"));
    return NULL;
}

int workq_init(workq_t *wq, int threads, void(*engine)(void *arg))
{
    int status = 0;

    status = pthread_attr_init(&wq->attr);
    if (0 != status) {
        return status;
    }

    status = pthread_attr_setdetachstate(&wq->attr, PTHREAD_CREATE_DETACHED);
    if (0 != status) {
        pthread_attr_destroy(&wq->attr);
        return status;
    }

    status = pthread_mutex_init(&wq->mutex, NULL);
    if (0 != status) {
        pthread_attr_destroy(&wq->attr);
        return status;
    }

    status = pthread_cond_init(&wq->cv, NULL);
    if (0 != status) {
        pthread_mutex_destroy(&wq->mutex);
        pthread_attr_destroy(&wq->attr);
        return status;
    }

    wq->quit = 0;
    wq->idle = 0;
    wq->counter = 0;
    wq->last = NULL;
    wq->first = NULL;
    wq->engine = engine;
    wq->parallelism = threads;
    wq->self = WORKQ_VALID;
    return 0;
}

static int _workq_wait_finish(workq_t *wq)
{
    int status = 0;
    
    if (wq->idle > 0) {
        status = pthread_cond_broadcast(&wq->cv);
        if (0 != status) {
            pthread_mutex_unlock(&wq->mutex);
            return status;
        }
    }

    while (wq->counter > 0) {
        status = pthread_cond_wait(&wq->cv, &wq->mutex);
        if (0 != status) {
            pthread_mutex_unlock(&wq->mutex);
            return status;
        }
    }

    return status;
}

int workq_destroy(workq_t *wq)
{
    int status = 0;
    int cond_rc = 0;
    int attr_rc = 0;

    if (wq->self != WORKQ_VALID) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&wq->mutex);
    if (0 != status) {
        return status;
    }

    wq->self = 0;
    if (wq->counter > 0) {
        wq->quit = 1;
        status = _workq_wait_finish(wq);

        if (0 != status) {
            return status;
        }
    }

    status = pthread_mutex_unlock(&wq->mutex);
    if (0 != status) {
        return status;
    }

    status = pthread_mutex_destroy(&wq->mutex);
    cond_rc = pthread_cond_destroy(&wq->cv);
    attr_rc = pthread_attr_destroy(&wq->attr);

    if (0 != status) {
        return status;
    }

    if (0 != cond_rc) {
        return cond_rc;
    }

    return attr_rc;
}

int workq_add(workq_t *wq, void *element)
{
    pthread_t id;
    int status = 0;
    workq_ele_t *item = NULL;

    if (WORKQ_VALID != wq->self) {
        return EINVAL;
    }

    item = (workq_ele_t*)malloc(sizeof(*item));
    if (NULL == item) {
        return ENOMEM;
    }

    item->data = element;
    item->next = NULL;

    status = pthread_mutex_lock(&wq->mutex);
    if (0 != status) {
        free(item);
        return status;
    }

    if (NULL == wq->first) {
        wq->first = item;
    } else {
        wq->last->next = item;
    }

    wq->last = item;
    if (wq->idle > 0) {
        status = pthread_cond_signal(&wq->cv);
        if (0 != status) {
            pthread_mutex_unlock(&wq->mutex);
            return status;
        }
    } else if (wq->counter < wq->parallelism) {
        DPRINTF(("Creating new worker\n"));
        status = pthread_create(&id, &wq->attr, workq_server,(void*)wq);
        if (0 != status) {
            pthread_mutex_unlock(&wq->mutex);
            return status;
        }

        wq->counter++;
    }

    pthread_mutex_unlock(&wq->mutex);
    return 0;
}
