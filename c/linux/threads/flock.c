#include "errors.h"
#include <pthread.h>

static void *prompt_routine(void *arg)
{
    int len = 0;
    char *string = NULL;
    char *prompt = (char*)arg;

    string = (char*)malloc(128);
    if (NULL == string) {
        errno_abort("Alloc string");
    }

    flockfile(stdin);
    flockfile(stdout);
    printf("%s", prompt);

    if (NULL == fgets(string, 128, stdin)) {
        string[0] = '\0';
    } else {
        len = strlen(string);
        if (len>0 && string[len-1]=='\n') {
            string[len-1] = '\0';
        }
    }

    funlockfile(stdout);
    funlockfile(stdin);
    return(void*)string;
}

int main(int argc, char *argv[])
{
    pthread_t thread1;
    pthread_t thread2;
    pthread_t thread3;
    char *string = NULL;
    int status = 0;

    status = pthread_create(&thread1, NULL, prompt_routine, "Thread 1> ");
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    status = pthread_create(&thread2, NULL, prompt_routine, "Thread 2> ");
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    status = pthread_create(&thread3, NULL, prompt_routine, "Thread 3> ");
    if (0 != status) {
        err_abort(status, "Create thread");
    }

    status = pthread_join(thread1,(void**)&string);
    if (0 != status) {
        err_abort(status, "Join thread");
    }

    printf("Thread 1: \"%s\"\n", string);
    free(string);
    status = pthread_join(thread2,(void**)&string);

    if (0 != status) {
        err_abort(status, "Join thread");
    }

    printf("Thread 1: \"%s\"\n", string);
    free(string);
    status = pthread_join(thread3,(void**)&string);

    if (0 != status) {
        err_abort(status, "Join thread");
    }

    printf("Thread 1: \"%s\"\n", string);
    free(string);
    return 0;
}
