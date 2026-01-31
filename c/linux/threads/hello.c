#include "errors.h"
#include <pthread.h>

static void *hello_world(void *arg)
{
    printf("Hello world\n");
    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t hello_id;

    status = pthread_create(&hello_id, NULL, hello_world, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    status = pthread_join(hello_id, NULL);
    if (0 != status) {
        err_abort(status, "Join thread");
    }

    return 0;
}
