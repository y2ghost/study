#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

static int cancel_operation = 0;
static pthread_cond_t action_cond = PTHREAD_COND_INITIALIZER;
static pthread_mutex_t action_mutex = PTHREAD_MUTEX_INITIALIZER;

void restore_coocked_mode(void *dummy)
{
    system("stty -raw echo");
}

void *read_user_input(void *data)
{
    int c = 0;

    pthread_cleanup_push(restore_coocked_mode, NULL);
    pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, NULL);
    system("stty raw -echo");

    while (1) {
        c = getchar();
        if (EOF == c) {
            break;
        }

        if ('e' == c) {
            cancel_operation = 1;
            pthread_cond_signal(&action_cond);
            pthread_exit(NULL);
        }
    }

    pthread_cleanup_pop(1);
    return NULL;
}

void *file_line_count(void *data)
{
    char *data_file = (char*)data;
    FILE *fp = fopen(data_file, "r");
    int wc = 0;
    int c = 0;

    if (NULL == fp) {
        perror("fopen");
        exit(1);
    }

    pthread_setcanceltype(PTHREAD_CANCEL_ASYNCHRONOUS, NULL);
    while (1) {
        c = getc(fp);
        if (EOF == c) {
            break;
        }

        if ('\n' == c) {
            wc++;
        }
    }

    fclose(fp);
    /* signify that we are done. */
    pthread_cond_signal(&action_cond);
    return (void*)wc;
}

int main(int argc, char* argv[])
{
    char *data_file = NULL;
    pthread_t thread_line_count;
    pthread_t thread_user_input;
    void *line_count = NULL;

    if (2 != argc) {
        fprintf(stderr, "Usage: %s <data file name>\n", argv[0]);
        return -1;
    }

    data_file = argv[1];
    printf("Checking file size (press 'e' to cancel operation)...");
    fflush(stdout);

    /* spawn the line counting thread */
    pthread_create(&thread_line_count, NULL, file_line_count, (void*)data_file);
    /* spawn the user-reading thread */
    pthread_create(&thread_user_input, NULL, read_user_input, (void*)data_file);
    pthread_mutex_lock(&action_mutex);
    pthread_cond_wait(&action_cond, &action_mutex);
    pthread_mutex_unlock(&action_mutex);

    /* check if we were signaled due to user operation        */
    /* cancelling, or because the line-counting was finished. */
    if (cancel_operation) {
        /* we join it to make sure it restores normal */
        /* screen mode before we print out.           */
        pthread_join(thread_user_input, NULL);
        printf("operation canceled\n");
        fflush(stdout);
        /* cancel the file-checking thread */
        pthread_cancel(thread_line_count);
    } else {
        /* join the file line-counting thread, to get its results */
        pthread_join(thread_line_count, &line_count);

        /* cancel and join the user-input thread.     */
        /* we join it to make sure it restores normal */
        /* screen mode before we print out.           */
        pthread_cancel(thread_user_input);
        pthread_join(thread_user_input, NULL);
        /* and print the result */
        printf("'%d' lines.\n", (int)line_count);
    }

    return 0;
}
