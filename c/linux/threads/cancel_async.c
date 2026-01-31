#include "errors.h"
#include <pthread.h>

#define SIZE    10

static int matrixa[SIZE][SIZE];
static int matrixb[SIZE][SIZE];
static int matrixc[SIZE][SIZE];

#ifdef DEBUG
static void print_array(int matrix[SIZE][SIZE])
{
    int i = 0;
    int j = 0;
    int first = 0;

    for (i=0; i<SIZE; i++) {
        printf("[");
        first = 1;

        for (j=0; j<SIZE; j++) {
            if (0 == first) {
                printf(",");
            }

            printf("%x", matrix[i][j]);
            first = 0;
        }

        printf("]\n");
    }
}
#endif 

static void *thread_routine(void *arg)
{
    int i = 0;
    int j = 0;
    int k = 0;
    int status = 0;
    int cancel_type = 0;
    
    for (i=0; i<SIZE; i++) {
        for (j=0; j<SIZE; j++) {
            matrixa[i][j] = i;
            matrixb[i][j] = j;
        }
    }

    while (1) {
        status = pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, &cancel_type);
        if (0 != status) {
            err_abort(status, "Set cancel type");
        }

        for (i=0; i<SIZE; i++) {
            for (j=0; j<SIZE; j++) {
                matrixc[i][j] = 0;
                for (k=0; k<SIZE; k++) {
                    matrixc[i][j] += matrixa[i][k] * matrixb[k][j];
                }
            }
        }

        status = pthread_setcanceltype(cancel_type, &cancel_type);
        if (0 != status) {
            err_abort(status, "Set cancel type");
        }

        for (i=0; i<SIZE; i++) {
            for (j=0; j<SIZE; j++) {
                matrixa[i][j] = matrixc[i][j];
            }
        }
    }
}

int main(int argc, char *argv[])
{
    int status = 0;
    void *result = NULL;
    pthread_t thread_id;

    status = pthread_create(&thread_id, NULL, thread_routine, NULL);
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    sleep(1);
    status = pthread_cancel(thread_id);

    if (0 != status) {
        err_abort(status, "Cancel thread");
    }

    status = pthread_join(thread_id, &result);
    if (0 != status) {
        err_abort(status, "Join thread");
    }

    if (PTHREAD_CANCELED == result) {
        printf("Thread cancelled\n");
    } else {
        printf("Thread was not cancelled\n");
    }

#ifdef DEBUG
    printf("Matrix a:\n");
    print_array(matrixa);
    printf("\nMatrix b:\n");
    print_array(matrixb);
    printf("\nMatrix c:\n");
    print_array(matrixc);
#endif 
    return 0;
}
