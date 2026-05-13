#ifndef BARRIER_H_
#define BARRIER_H_

#include <pthread.h>

#define BARRIER_VALID   0xdbcafe

typedef struct barrier_tag {
    int self;
    int cycle;
    int counter;
    int threshold;
    pthread_cond_t cv;
    pthread_mutex_t mutex;
} barrier_t;

int barrier_init(barrier_t *barrier, int count);
int barrier_destroy(barrier_t *barrier);
int barrier_wait(barrier_t *barrier);

#endif /* BARRIER_H_ */
