#include "workq.h"
#include "errors.h"
#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <time.h>

#define ITERATIONS  25

typedef struct power_tag {
    int value;
    int power;
} power_t;

typedef struct engine_tag {
    struct engine_tag *link;
    pthread_t thread_id;
    int calls;
} engine_t;

static pthread_key_t engine_key;
static pthread_mutex_t engine_list_mutex = PTHREAD_MUTEX_INITIALIZER;
static engine_t *engine_list_head = NULL;
static workq_t workq;

static void destructor(void *value_ptr)
{
    engine_t *engine = (engine_t*)value_ptr;

    pthread_mutex_lock(&engine_list_mutex);
    engine->link = engine_list_head;
    engine_list_head = engine;
    pthread_mutex_unlock(&engine_list_mutex);
}

static void engine_routine(void *arg)
{
    engine_t *engine = NULL;
    power_t *power = (power_t*)arg;
    int result = 0;
    int count = 0;
    int status = 0;

    engine = pthread_getspecific(engine_key);
    if (NULL == engine) {
        engine =(engine_t*)malloc(sizeof(engine_t));
        status = pthread_setspecific(engine_key,(void*)engine);

        if (0 != status) {
            err_abort(status, "Set tsd");
        }

        engine->thread_id = pthread_self();
        engine->calls = 1;
    } else {
        engine->calls++;
    }

    result = 1;
    printf("Engine: computing %d^%d\n", power->value, power->power);

    for (count=1; count<=power->power; count++) {
        result *= power->value;
    }

    free(arg);
}

static void *thread_routine(void *arg)
{
    power_t *element = NULL;
    int count = 0;
    unsigned int seed = (unsigned int)time(NULL);
    int status = 0;

    for (count=0; count<ITERATIONS; count++) {
        element = (power_t*)malloc(sizeof(*element));
        if (NULL == element) {
            errno_abort("Allocate element");
        }

        element->value = rand_r(&seed) % 20;
        element->power = rand_r(&seed) % 7;
        DPRINTF(("Request: %d^%d\n",
            element->value, element->power));
        status = workq_add(&workq, (void*)element);

        if (0 != status) {
            err_abort(status, "Add to work queue");
        }

        sleep(rand_r(&seed) % 5);
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    pthread_t thread_id;
    engine_t *engine = NULL;
    int count = 0;
    int calls = 0;
    int status = 0;

    status = pthread_key_create(&engine_key, destructor);
    if (0 != status) {
        err_abort(status, "Create key");
    }

    status = workq_init(&workq, 4, engine_routine);
    if (0 != status) {
        err_abort(status, "Init work queue");
    }

    status = pthread_create(&thread_id, NULL, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    thread_routine(NULL);
    status = pthread_join(thread_id, NULL);

    if (0 != status) {
        err_abort(status, "Join thread");
    }

    status = workq_destroy(&workq);
    if (0 != status) {
        err_abort(status, "Destroy work queue");
    }

    engine = engine_list_head;
    while (NULL != engine) {
        count++;
        calls += engine->calls;
        printf("engine %d: %d calls\n", count, engine->calls);
        engine = engine->link;
    }

    printf("%d engine threads processed %d calls\n",
        count, calls);
    return 0;
}
