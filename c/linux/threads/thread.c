#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

static void *writer_thread(void *arg)
{
    sleep(5);
    printf("Thread I/O\n");
    return NULL;
}

int main(int argc, char *argv[])
{
    pthread_t   writer_id;
    char *input = NULL;
    char buffer[64] = {'\0'};

    pthread_create(&writer_id, NULL, writer_thread, NULL);
    input = fgets(buffer, 64, stdin);

    if (NULL == input) {
        printf("You said %s", buffer);
    }

    pthread_exit(NULL);
    return 0;
}
