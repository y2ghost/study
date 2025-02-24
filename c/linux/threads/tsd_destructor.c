#include "errors.h"
#include <pthread.h>

typedef struct private_tag {
    char *string;
    pthread_t thread_id;
} private_t;

static long identity_key_counter = 0;
static pthread_key_t identity_key;
static pthread_mutex_t identity_key_mutex = PTHREAD_MUTEX_INITIALIZER;

static void identity_key_destructor(void *value)
{
    int status = 0;
    private_t *private = (private_t*)value;

    printf("thread (%s) exiting...\n", private->string);
    free(value);

    status = pthread_mutex_lock(&identity_key_mutex);
    if (0 != status) {
        err_abort(status, "Lock key mutex");
    }

    identity_key_counter--;
    if (identity_key_counter <= 0) {
        status = pthread_key_delete(identity_key);
        if (0 != status) {
            err_abort(status, "Delete key");
        }

        printf("key deleted...\n");
    }

    status = pthread_mutex_unlock(&identity_key_mutex);
    if (0 != status) {
        err_abort(status, "Unlock key mutex");
    }
}

static void *identity_key_get(void)
{
    int status = 0;
    private_t *value = 0;

    value = pthread_getspecific(identity_key);
    if (NULL == value) {
        value = malloc(sizeof(*value));
        if (NULL == value) {
            errno_abort("Allocate key value");
        }

        status = pthread_setspecific(identity_key, (void*)value);
        if (0 != status) {
            err_abort(status, "Set TSD");
        }
    }

    return value;
}

static void *thread_routine(void *arg)
{
    private_t *value = NULL;

    value = (private_t*)identity_key_get();
    value->thread_id = pthread_self();
    value->string =(char*)arg;
    printf("thread \"%s\" starting...\n", value->string);
    sleep(2);
    return NULL;    
}

int main(int argc, char *argv[])
{
    int status = 0;
    private_t *value = NULL;
    pthread_t thread_1;
    pthread_t thread_2;

    status = pthread_key_create(&identity_key, identity_key_destructor);
    if (0 != status) {
        err_abort(status, "Create key");
    }

    identity_key_counter = 3;
    value =(private_t*)identity_key_get();
    value->thread_id = pthread_self();
    value->string = "Main thread";
    status = pthread_create(&thread_1, NULL, thread_routine, "Thread 1");
    if (0 != status) {
        err_abort(status, "Create thread 1");
    }

    status = pthread_create(&thread_2, NULL, thread_routine, "Thread 2");
    if (0 != status) {
        err_abort(status, "Create thread 2");
    }

    pthread_exit(NULL);
}
