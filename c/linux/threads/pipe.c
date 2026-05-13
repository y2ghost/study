#include "errors.h"
#include <pthread.h>

typedef struct stage_tag {
    long data;
    int data_ready;
    pthread_t thread;
    pthread_cond_t avail;
    pthread_cond_t ready;
    pthread_mutex_t mutex;
    struct stage_tag *next;
} stage_t;

typedef struct pipe_tag {
    int stages;
    int active;
    stage_t *head;
    stage_t *tail;
    pthread_mutex_t mutex;
} pipe_t;

static int pipe_send(stage_t *stage, long data)
{
    int status = 0;

    status = pthread_mutex_lock(&stage->mutex);
    if (0 != status) {
        return status;
    }

    /* it there's data in the stage, wait for it to be consumed */
    while (0 != stage->data_ready) {
        status = pthread_cond_wait(&stage->ready, &stage->mutex);
        if (0 != status) {
            pthread_mutex_unlock(&stage->mutex);
            return status;
        }
    }

    stage->data = data;
    stage->data_ready = 1;
    status = pthread_cond_signal(&stage->avail);

    if (0 != status) {
        pthread_mutex_unlock(&stage->mutex);
        return status;
    }

    status = pthread_mutex_unlock(&stage->mutex);
    return status;
}

static void *pipe_stage(void *arg)
{
    stage_t *stage = (stage_t*)arg;
    stage_t *next_stage = stage->next;
    int status = 0;

    status = pthread_mutex_lock(&stage->mutex);
    if (0 != status) {
        err_abort(status, "Lock pipe stage");
    }

    while (1) {
        while (0 == stage->data_ready) {
            status = pthread_cond_wait(&stage->avail, &stage->mutex);
            if (0 != status) {
                err_abort(status, "Wait for previous stage");
            }
        }

        pipe_send(next_stage, stage->data + 1);
        stage->data_ready = 0;
        status = pthread_cond_signal(&stage->ready);

        if (0 != status) {
            err_abort(status, "Wake next stage");
        }
    }

    /* never be here, pthread_cond_wait implicitly unlocks the mutex */
    status = pthread_mutex_unlock(&stage->mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }
}

static int pipe_create(pipe_t *pipe, int stages)
{
    int status = 0;
    int pipe_index = 0;
    stage_t *stage = NULL;
    stage_t *new_stage = NULL;
    stage_t **link = &pipe->head;

    status = pthread_mutex_init(&pipe->mutex, NULL);
    if (0 != status) {
        err_abort(status, "Init pipe mutex");
    }

    pipe->active = 0;
    pipe->stages = stages;

    for (pipe_index=0; pipe_index<=stages; pipe_index++) {
        new_stage = (stage_t*)malloc(sizeof(*new_stage));
        if (NULL == new_stage) {
            errno_abort("Allocate stage");
        }

        status = pthread_mutex_init(&new_stage->mutex, NULL);
        if (0 != status) {
            err_abort(status, "Init stage mutex");
        }

        status = pthread_cond_init(&new_stage->avail, NULL);
        if (0 != status) {
            err_abort(status, "Init avail condition");
        }

        status = pthread_cond_init(&new_stage->ready, NULL);
        if (0 != status) {
            err_abort(status, "Init ready condition");
        }

        new_stage->data_ready = 0;
        *link = new_stage;
        link = &new_stage->next;
    }

    *link = NULL;
    pipe->tail = new_stage;

    for (stage=pipe->head; NULL!=stage->next; stage=stage->next) {
        status = pthread_create(&stage->thread, NULL, pipe_stage, (void*)stage);

        if (0 != status) {
            err_abort(status, "Create pipe stage");
        }
    }

    return 0;
}

static int pipe_start(pipe_t *pipe, long value)
{
    int status = 0;

    status = pthread_mutex_lock(&pipe->mutex);
    if (0 != status) {
        err_abort(status, "Lock pipe mutex");
    }

    if (pipe->active >= pipe->stages) {
        printf("stages full now, please enter '=' to consume\n");
        status = pthread_mutex_unlock(&pipe->mutex);

        if (0 != status) {
            err_abort(status, "Unlock pipe mutex");
        }

        return 1;
    }

    pipe->active++;
    status = pthread_mutex_unlock(&pipe->mutex);

    if (0 != status) {
        err_abort(status, "Unlock pipe mutex");
    }

    pipe_send(pipe->head, value);
    return 0;
}

static int pipe_result(pipe_t *pipe, long *result)
{
    int empty = 0;
    int status = 0;
    stage_t *tail = pipe->tail;

    status = pthread_mutex_lock(&pipe->mutex);
    if (0 != status) {
        err_abort(status, "Lock pipe mutex");
    }

    if (pipe->active <= 0) {
        empty = 1;
    } else {
        pipe->active--;
    }

    status = pthread_mutex_unlock(&pipe->mutex);
    if (0 != status) {
        err_abort(status, "Unlock pipe mutex");
    }

    if (0 != empty) {
        return 0;
    }

    pthread_mutex_lock(&tail->mutex);
    while (0 == tail->data_ready) {
        pthread_cond_wait(&tail->avail, &tail->mutex);
    }

    *result = tail->data;
    tail->data_ready = 0;
    pthread_cond_signal(&tail->ready);
    pthread_mutex_unlock(&tail->mutex);    
    return 1;
}

int main(int argc, char *argv[])
{
    pipe_t my_pipe;
    long value = 0;
    long result = 0;
    char line[128] = {'\0'};

    pipe_create(&my_pipe, 10);
    printf("Enter integer values, or \"=\" for next result\n");

    while (1) {
        int rc = 0;
        char *buf = NULL;

        printf("Data> ");
        buf = fgets(line, sizeof(line), stdin);

        if (NULL == buf) {
            exit(0);
        }

        if (strlen(buf) <= 1) {
            continue;
        }

        if (strlen(buf)<=2 && line[0]=='=') {
            if (pipe_result(&my_pipe, &result)) {
                printf("Result is %ld\n", result);
            } else {
                printf("Pipe is empty\n");
            }

            continue;
        }

        rc= sscanf(line, "%ld", &value);
        if (rc < 1) {
            fprintf(stderr, "Enter an integer value\n");
        } else {
            pipe_start(&my_pipe, value);
        }
    }

    return 0;
}
