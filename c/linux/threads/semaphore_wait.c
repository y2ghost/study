#include "errors.h"
#include <unistd.h>
#include <pthread.h>
#include <sys/types.h>
#include <semaphore.h>
#include <signal.h>

static sem_t semaphore;

static void *sem_waiter(void *arg)
{
    long num = *(long*)arg;

    printf("Thread %ld waiting\n", num);
    if (-1 == sem_wait(&semaphore)) {
        errno_abort("Wait on semaphore");
    }

    printf("Thread %ld resuming\n", num);
    return NULL;
}

int main(int argc, char *argv[])
{
    long thread_count = 0;
    pthread_t sem_waiters[5];
    int status = 0;

#if  !defined(_POSIX_SEMAPHORES)
    printf("This system does not support POSIX semaphores\n");
    return -1;
#else

    if (-1 == sem_init(&semaphore, 0, 0)) {
        errno_abort("Init semaphore");
    }

    for (thread_count=0; thread_count<5; thread_count++) {
        status = pthread_create(&sem_waiters[thread_count], NULL,
            sem_waiter, &thread_count);
        if (0 != status) {
            err_abort(status, "Create thread");
        }
    }

    sleep(2);
    while (1) {
        int sem_value = 0;

        if (-1 == sem_getvalue(&semaphore, &sem_value)) {
            errno_abort("Get semaphore value");
        }

        if (sem_value >= 0) {
            break;
        }

        printf("Posting from main: %d\n", sem_value);
        if (-1 == sem_post(&semaphore)) {
            errno_abort("Post semaphore");
        }
    }

    for (thread_count=0; thread_count<5; thread_count++) {
        status = pthread_join(sem_waiters[thread_count], NULL);
        if (0 != status) {
            err_abort(status, "Join thread");
        }
    }

    return 0;
#endif 
}
