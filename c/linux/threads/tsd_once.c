#include "errors.h"
#include <pthread.h>

typedef struct tsd_tag {
    char *string;
    pthread_t thread_id;
} tsd_t;

static pthread_key_t tsd_key;
static pthread_once_t key_once = PTHREAD_ONCE_INIT;

static void once_routine(void)
{
    int status = 0;

    printf("initializing key\n");
    status = pthread_key_create(&tsd_key, NULL);

    if (0 != status) {
        err_abort(status, "Create key");
    }
}

static void *thread_routine(void *arg)
{
    int status = 0;
    tsd_t *value = NULL;

    status = pthread_once(&key_once, once_routine);
    if (0 != status) {
        err_abort(status, "Once init");
    }

    value = (tsd_t*)malloc(sizeof(*value));
    if (NULL == value) {
        errno_abort("Allocate key value");
    }

    status = pthread_setspecific(tsd_key, value);
    if (0 != status) {
        err_abort(status, "Set tsd");
    }

    printf("%s set tsd value %p\n", (char*)arg, value);
    value->thread_id = pthread_self();
    value->string = (char*)arg;
    value = (tsd_t*)pthread_getspecific(tsd_key);
    printf("%s starting...\n", value->string);
    sleep(2);
    value = (tsd_t*)pthread_getspecific(tsd_key);
    printf("%s done...\n", value->string);
    free(value);
    return NULL;    
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t thread1;
    pthread_t thread2;

    status = pthread_create(&thread1, NULL, thread_routine, "thread_1_private");
    if (0 != status) {
        err_abort(status, "Create thread 1");
    }

    status = pthread_create(&thread2, NULL, thread_routine, "thread_2_private");
    if (0 != status) {
        err_abort(status, "Create thread 2");
    }

    pthread_exit(NULL);
}
