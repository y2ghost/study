#include "errors.h"
#include <pthread.h>

typedef struct my_struct_tag {
    pthread_mutex_t mutex;
    int value;
} my_struct_t;

int main(int argc, char *argv[])
{
    my_struct_t *data = NULL;
    int status = 0;

    data = malloc(sizeof(*data));
    if (NULL == data) {
        errno_abort("Allocate structure");
    }

    status = pthread_mutex_init(&data->mutex, NULL);
    if (0 != status) {
        err_abort(status, "Init mutex");
    }

    status = pthread_mutex_destroy(&data->mutex);
    if (0 != status) {
        err_abort(status, "Destroy mutex");
    }

    free(data);
    return status;
}
