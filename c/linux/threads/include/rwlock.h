#ifndef RWLOCK_H_
#define RWLOCK_H_

#include <pthread.h>

#define RWLOCK_VALID    0xfacade

typedef struct rwlock_tag {
    int self;
    int r_wait;
    int w_wait;
    int r_active;
    int w_active;
    pthread_cond_t read;
    pthread_cond_t write;
    pthread_mutex_t mutex;
} rwlock_t;

int rwl_init(rwlock_t *rwlock);
int rwl_destroy(rwlock_t *rwlock);
int rwl_readlock(rwlock_t *rwlock);
int rwl_readtrylock(rwlock_t *rwlock);
int rwl_readunlock(rwlock_t *rwlock);
int rwl_writelock(rwlock_t *rwlock);
int rwl_writetrylock(rwlock_t *rwlock);
int rwl_writeunlock(rwlock_t *rwlock);

#endif /* RWLOCK_H_ */
