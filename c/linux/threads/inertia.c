#include "errors.h"
#include <pthread.h>

static void *printer_thread(void *arg)
{
    char *string = *(char**)arg;

    printf("%s\n", string);
    return NULL;
}

int main(int argc, char *argv[])
{
    int i = 0;
    int status = 0;
    pthread_t printer_id;
    char *string_ptr = NULL;

    string_ptr = "Before value";
    status = pthread_create(&printer_id, NULL, printer_thread,(void*)&string_ptr);

    if (0 != status) {
        err_abort(status, "Create thread");
    }

    for (i=0; i<10000000; i++) {
        continue;
    }

    string_ptr = "After value";
    status = pthread_join(printer_id, NULL);

    if (0 != status) {
        err_abort(status, "Join thread");
    }

    return 0;
}
