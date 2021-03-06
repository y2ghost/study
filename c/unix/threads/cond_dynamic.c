#include "errors.h"
#include <pthread.h>

typedef struct my_struct_tag {
    pthread_mutex_t mutex;
    pthread_cond_t cond;
    int value;
} my_struct_t;

int main(int argc, char *argv[])
{
    int status = 0;
    my_struct_t *data = NULL;

    data = (my_struct_t*)malloc(sizeof(*data));
    if (NULL == data) {
        errno_abort("Allocate structure");
    }

    status = pthread_mutex_init(&data->mutex, NULL);
    if (0 != status) {
        err_abort(status, "Init mutex");
    }

    status = pthread_cond_init(&data->cond, NULL);
    if (0 != status) {
        err_abort(status, "Init condition");
    }

    status = pthread_cond_destroy(&data->cond);
    if (0 != status) {
        err_abort(status, "Destroy condition");
    }

    status = pthread_mutex_destroy(&data->mutex);
    if (0 != status) {
        err_abort(status, "Destroy mutex");
    }

    free(data);
    return status;
}
