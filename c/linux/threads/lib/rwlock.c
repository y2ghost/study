#include "rwlock.h"
#include "errors.h"
#include <pthread.h>

int rwl_init(rwlock_t *rwl)
{
    int status = 0;

    rwl->r_wait = 0;
    rwl->w_wait = 0;
    rwl->r_active = 0;
    rwl->w_active = 0;

    status = pthread_mutex_init(&rwl->mutex, NULL);
    if (0 != status) {
        return status;
    }

    status = pthread_cond_init(&rwl->read, NULL);
    if (0 != status) {
        pthread_mutex_destroy(&rwl->mutex);
        return status;
    }

    status = pthread_cond_init(&rwl->write, NULL);
    if (0 != status) {
        pthread_cond_destroy(&rwl->read);
        pthread_mutex_destroy(&rwl->mutex);
        return status;
    }

    rwl->self = RWLOCK_VALID;
    return 0;
}

int rwl_destroy(rwlock_t *rwl)
{
    int status = 0;
    int read_cond_rc = 0;
    int writ_cond_rc = 0;

    if (rwl->self != RWLOCK_VALID) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&rwl->mutex);
    if (0 != status) {
        return status;
    }

    if (rwl->r_active>0 || 0!=rwl->w_active) {
        pthread_mutex_unlock(&rwl->mutex);
        return EBUSY;
    }

    if (0!=rwl->r_wait || 0!=rwl->w_wait) {
        pthread_mutex_unlock(&rwl->mutex);
        return EBUSY;
    }

    rwl->self = 0;
    status = pthread_mutex_unlock(&rwl->mutex);

    if (0 != status) {
        return status;
    }

    status = pthread_mutex_destroy(&rwl->mutex);
    read_cond_rc = pthread_cond_destroy(&rwl->read);
    writ_cond_rc = pthread_cond_destroy(&rwl->write);

    if (0 != status) {
        return status;
    }

    if (0 != read_cond_rc) {
        return read_cond_rc;
    }

    return writ_cond_rc;
}

static void rwl_readcleanup(void *arg)
{
    rwlock_t *rwl = (rwlock_t*)arg;

    rwl->r_wait--;
    pthread_mutex_unlock(&rwl->mutex);
}

static void rwl_writecleanup(void *arg)
{
    rwlock_t *rwl = (rwlock_t*)arg;

    rwl->w_wait--;
    pthread_mutex_unlock(&rwl->mutex);
}

int rwl_readlock(rwlock_t *rwl)
{
    int status = 0;

    if (RWLOCK_VALID != rwl->self) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&rwl->mutex);
    if (0 != status) {
        return status;
    }

    if (0 != rwl->w_active) {
        rwl->r_wait++;
        pthread_cleanup_push(rwl_readcleanup, (void*)rwl);

        while (0 != rwl->w_active) {
            status = pthread_cond_wait(&rwl->read, &rwl->mutex);
            if (0 != status) {
                break;
            }
        }

        pthread_cleanup_pop(0);
        rwl->r_wait--;
    }

    if (0 == status) {
        rwl->r_active++;
    }

    pthread_mutex_unlock(&rwl->mutex);
    return status;
}

int rwl_readtrylock(rwlock_t *rwl)
{
    int try_ok = 0;
    int unlock_ok = 0;

    if (RWLOCK_VALID != rwl->self) {
        return EINVAL;
    }

    try_ok = pthread_mutex_lock(&rwl->mutex);
    if (0 != try_ok) {
        return try_ok;
    }

    if (rwl->w_active) {
        try_ok = EBUSY;
    } else {
        rwl->r_active++;
    }

    unlock_ok = pthread_mutex_unlock(&rwl->mutex);
    if (0 == unlock_ok) {
        try_ok = unlock_ok;
    }

    return try_ok;
}

int rwl_readunlock(rwlock_t *rwl)
{
    int try_ok = 0;
    int unlock_ok = 0;

    if (RWLOCK_VALID != rwl->self) {
        return EINVAL;
    }

    try_ok = pthread_mutex_lock(&rwl->mutex);
    if (0 != try_ok) {
        return try_ok;
    }

    rwl->r_active--;
    if (0==rwl->r_active && rwl->w_wait>0) {
        try_ok = pthread_cond_signal(&rwl->write);
    }

    unlock_ok = pthread_mutex_unlock(&rwl->mutex);
    if (0 != unlock_ok) {
        try_ok = unlock_ok;
    }

    return try_ok;
}
int rwl_writelock(rwlock_t *rwl)
{
    int status = 0;

    if (RWLOCK_VALID != rwl->self) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&rwl->mutex);
    if (0 != status) {
        return status;
    }

    if (0!=rwl->w_active || rwl->r_active>0) {
        rwl->w_wait++;
        pthread_cleanup_push(rwl_writecleanup, (void*)rwl);

        while (0!=rwl->w_active || rwl->r_active > 0) {
            status = pthread_cond_wait(&rwl->write, &rwl->mutex);
            if (0 != status) {
                break;
            }
        }

        pthread_cleanup_pop(0);
        rwl->w_wait--;
    }

    if (status == 0) {
        rwl->w_active = 1;
    }

    pthread_mutex_unlock(&rwl->mutex);
    return status;
}

int rwl_writetrylock(rwlock_t *rwl)
{
    int status = 0;
    int unlock_ok = 0;

    if (RWLOCK_VALID != rwl->self) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&rwl->mutex);
    if (0 != status) {
        return status;
    }

    if (0!=rwl->w_active || rwl->r_active>0) {
        status = EBUSY;
    } else {
        rwl->w_active = 1;
    }

    unlock_ok = pthread_mutex_unlock(&rwl->mutex);
    if (0 != status) {
        status = unlock_ok;
    }

    return status;
}

int rwl_writeunlock(rwlock_t *rwl)
{
    int status = 0;

    if (RWLOCK_VALID != rwl->self) {
        return EINVAL;
    }

    status = pthread_mutex_lock(&rwl->mutex);
    if (0 != status) {
        return status;
    }

    rwl->w_active = 0;
    if (rwl->r_wait > 0) {
        status = pthread_cond_broadcast(&rwl->read);
        if (0 != status) {
            pthread_mutex_unlock(&rwl->mutex);
            return status;
        }
    } else if (rwl->w_wait > 0) {
        status = pthread_cond_signal(&rwl->write);
        if (0 != status) {
            pthread_mutex_unlock(&rwl->mutex);
            return status;
        }
    }

    status = pthread_mutex_unlock(&rwl->mutex);
    return status;
}
