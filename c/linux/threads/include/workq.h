#ifndef WORKQ_H_
#define WORKQ_H_

#include <pthread.h>

#define WORKQ_VALID 0xdec1992

typedef struct workq_ele_tag {
    void *data;
    struct workq_ele_tag *next;
} workq_ele_t;

typedef struct workq_tag {
    int quit;
    int idle;
    int self;
    int counter;
    int parallelism;
    workq_ele_t *first;
    workq_ele_t *last;
    pthread_cond_t cv;
    pthread_attr_t attr;
    pthread_mutex_t mutex;
    void(*engine)(void *arg);
} workq_t;

int workq_init(workq_t *wq, int threads, void(*engine)(void*));
int workq_add(workq_t *wq, void *data);
int workq_destroy(workq_t *wq);

#endif /* WORKQ_H_ */
