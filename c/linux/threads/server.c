#include "errors.h"
#include <math.h>
#include <pthread.h>

#define CLIENT_THREADS  4
#define REQ_READ        1
#define REQ_WRITE       2
#define REQ_QUIT        3

typedef struct request_tag {
    int operation;
    int synchronous;
    int done_flag;
    pthread_cond_t done;
    char prompt[32];
    char text[128];
    struct request_tag *next;
} request_t;

typedef struct tty_server_tag {
    request_t *first;
    request_t *last;
    int running;
    pthread_mutex_t mutex;
    pthread_cond_t request;
} tty_server_t;

static tty_server_t tty_server = {
    NULL, NULL, 0,
    PTHREAD_MUTEX_INITIALIZER, PTHREAD_COND_INITIALIZER};

static int client_threads = 0;
static pthread_mutex_t client_mutex = PTHREAD_MUTEX_INITIALIZER;
static pthread_cond_t clients_done = PTHREAD_COND_INITIALIZER;

static void *tty_server_routine(void *arg)
{
    int len = 0;
    int status = 0;
    int operation = 0;
    request_t *request = NULL;

    while (1) {
        status = pthread_mutex_lock(&tty_server.mutex);
        if (0 != status) {
            err_abort(status, "Lock server mutex");
        }

        while (NULL == tty_server.first) {
            status = pthread_cond_wait(&tty_server.request, &tty_server.mutex);
            if (0 != status) {
                err_abort(status, "Wait for request");
            }
        }

        request = tty_server.first;
        tty_server.first = request->next;

        if (NULL == tty_server.first) {
            tty_server.last = NULL;
        }

        status = pthread_mutex_unlock(&tty_server.mutex);
        if (0 != status) {
            err_abort(status, "Unlock server mutex");
        }

        operation = request->operation;
        switch(operation) {
        case REQ_QUIT:
            break;
        case REQ_READ:
            if (strlen(request->prompt) > 0) {
                printf("%s", request->prompt);
            }

            if (NULL == fgets(request->text, sizeof(request->text), stdin)) {
                request->text[0] = '\0';
            }

            len = strlen(request->text);
            if (len>0 && request->text[len-1]=='\n') {
                request->text[len-1] = '\0';
            }

            break;
        case REQ_WRITE:
            puts(request->text);
            break;
        default:
            break;
        }

        if (0 != request->synchronous) {
            status = pthread_mutex_lock(&tty_server.mutex);
            if (0 != status) {
                err_abort(status, "Lock server mutex");
            }

            request->done_flag = 1;
            status = pthread_cond_signal(&request->done);

            if (0 != status) {
                err_abort(status, "Signal server condition");
            }

            status = pthread_mutex_unlock(&tty_server.mutex);
            if (0 != status) {
                err_abort(status, "Unlock server mutex");
            }
        } else {
            free(request);
        }

        if (REQ_QUIT == operation) {
            break;
        }
    }

    printf("%s\n", "tty routine quit!");
    return NULL;
}

static void tty_server_request(int operation, int sync,
    const char *prompt, char *string)
{
    int status = 0;
    request_t *request = NULL;

    status = pthread_mutex_lock(&tty_server.mutex);
    if (0 != status) {
        err_abort(status, "Lock server mutex");
    }

    if (0 == tty_server.running) {
        pthread_t thread;
        pthread_attr_t detached_attr;

        status = pthread_attr_init(&detached_attr);
        if (0 != status) {
            err_abort(status, "Init attributes object");
        }

        status = pthread_attr_setdetachstate(&detached_attr,
            PTHREAD_CREATE_DETACHED);
        if (0 != status) {
            err_abort(status, "Set detach state");
        }

        status = pthread_create(&thread, &detached_attr,
            tty_server_routine, NULL);
        if (0 != status) {
            err_abort(status, "Create server");
        }

        tty_server.running = 1;
        pthread_attr_destroy(&detached_attr);
    }

    request =(request_t*)malloc(sizeof(*request));
    if (NULL == request) {
        errno_abort("Allocate request");
    }

    request->next = NULL;
    request->operation = operation;
    request->synchronous = sync;

    if (0 != sync) {
        request->done_flag = 0;
        status = pthread_cond_init(&request->done, NULL);

        if (0 != status) {
            err_abort(status, "Init request condition");
        }
    }

    if (NULL != prompt) {
        snprintf(request->prompt, sizeof(request->prompt), "%s", prompt);
    } else {
        request->prompt[0] = '\0';
    }

    if (REQ_WRITE==operation && NULL!=string) {
        snprintf(request->text, sizeof(request->text), "%s", string);
    } else {
        request->text[0] = '\0';
    }

    if (NULL == tty_server.first) {
        tty_server.first = request;
        tty_server.last = request;
    } else {
       (tty_server.last)->next = request;
        tty_server.last = request;
    }

    status = pthread_cond_signal(&tty_server.request);
    if (0 != status) {
        err_abort(status, "Wake server");
    }

    if (0 != sync) {
        while (0 == request->done_flag) {
            status = pthread_cond_wait(&request->done, &tty_server.mutex);
            if (0 != status) {
                err_abort(status, "Wait for sync request");
            }
        }

        if (REQ_READ == operation) {
            if (strlen(request->text) > 0) {
                strcpy(string, request->text);
            } else {
                string[0] = '\0';
            }
        }

        status = pthread_cond_destroy(&request->done);
        if (0 != status) {
            err_abort(status, "Destroy request condition");
        }

        free(request);
    }

    status = pthread_mutex_unlock(&tty_server.mutex);
    if (0 != status) {
        err_abort(status, "Unlock mutex");
    }
}

static void *client_routine(void *arg)
{
    int my_number = (int)arg;
    int loops = 0;
    int status = 0;
    char prompt[32] = {'\0'};
    char string[128] = {'\0'};
    char formatted[256] = {'\0'};

    sprintf(prompt, "Client %d> ", my_number);
    while (1) {
        tty_server_request(REQ_READ, 1, prompt, string);
        if (0 == strlen(string)) {
            break;
        }

        for (loops=0; loops<4; loops++) {
            snprintf(formatted, sizeof(formatted), "(%d#%d) %s",
                my_number, loops, string);
            tty_server_request(REQ_WRITE, 0, NULL, formatted);
            sleep(1);
        }
    }

    status = pthread_mutex_lock(&client_mutex);
    if (0 != status) {
        err_abort(status, "Lock client mutex");
    }

    client_threads--;
    if (client_threads <= 0) {
        status = pthread_cond_signal(&clients_done);
        if (0 != status) {
            err_abort(status, "Signal clients done");
        }
    }

    status = pthread_mutex_unlock(&client_mutex);
    if (0 != status) {
        err_abort(status, "Unlock client mutex");
    }

    printf("client thread: %d, quit!\n", my_number);
    return NULL;
}

int main(int argc, char *argv[])
{
    int count = 0;
    int status = 0;
    pthread_t thread;

    client_threads = CLIENT_THREADS;
    for (count=0; count<client_threads; count++) {
        status = pthread_create(&thread, NULL, client_routine, (void*)count);
        if (0 != status) {
            err_abort(status, "Create client thread");
        }
    }

    status = pthread_mutex_lock(&client_mutex);
    if (0 != status) {
        err_abort(status, "Lock client mutex");
    }

    while (client_threads > 0) {
        status = pthread_cond_wait(&clients_done, &client_mutex);
        if (0 != status) {
            err_abort(status, "Wait for clients to finish");
        }

    }

    status = pthread_mutex_unlock(&client_mutex);
    if (0 != status) {
        err_abort(status, "Unlock client mutex");
    }

    printf("All clients done\n");
    tty_server_request(REQ_QUIT, 1, NULL, NULL);
    return 0;
}
