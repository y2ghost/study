#include "errors.h"
#include <pthread.h>

typedef struct alarm_tag {
    int seconds;
    char message[64];
} alarm_t;

static void *alarm_thread(void *arg)
{
    int status = 0;
    alarm_t *alarm = (alarm_t*)arg;

    status = pthread_detach(pthread_self());
    if (0 != status) {
        err_abort(status, "Detach thread");
    }

    sleep(alarm->seconds);
    printf("(%d) %s\n", alarm->seconds, alarm->message);
    free(alarm);
    return NULL;
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t thread;
    alarm_t *alarm = NULL;
    char line[128] = {'\0'};

    while (1) {
        int rc = 0;
        char *buf = NULL;
        alarm_t temp_alarm;

        printf("Alarm> ");
        buf = fgets(line, sizeof(line), stdin);

        if (NULL == buf) {
            exit(0);
        }

        if (strlen(line) <= 1) {
            continue;
        }

        memset(&temp_alarm, 0x0, sizeof(temp_alarm));
        rc = sscanf(line, "%d %64[^\n]", &temp_alarm.seconds, temp_alarm.message);

        if (rc < 2) {
            fprintf(stderr, "Bad command\n");
            continue;
        }

        alarm = (alarm_t*)malloc(sizeof(*alarm));
        if (NULL == alarm) {
            errno_abort("Allocate alarm");
        }

        memcpy(alarm, &temp_alarm, sizeof(temp_alarm));
        status = pthread_create(&thread, NULL, alarm_thread, alarm);

        if (0 != status) {
            err_abort(status, "Create alarm thread");
        }
    }

    return 0;
}
