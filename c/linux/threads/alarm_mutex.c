#include "errors.h"
#include <time.h>
#include <pthread.h>

typedef struct alarm_tag {
    struct alarm_tag *link;
    int seconds;
    time_t time;
    char message[64];
} alarm_t;

static alarm_t *alarm_list = NULL;
static pthread_mutex_t alarm_mutex = PTHREAD_MUTEX_INITIALIZER;

static void *alarm_thread(void *arg)
{
    time_t now = 0;
    int status = 0;
    int sleep_time = 0;
    alarm_t *alarm = NULL;

    while (1) {
        status = pthread_mutex_lock(&alarm_mutex);
        if (0 != status) {
            err_abort(status, "Lock mutex");
        }

        /*
         * If the alarm list is empty, wait for one second. This
         * allows the main thread to run, and read another
         * command. If the list is not empty, remove the first
         * item. Compute the number of seconds to wait -- if  the
         * result is less than 0(the time has passed), then set
         * the sleep_time to 0.
         */
        alarm = alarm_list;
        if (NULL == alarm) {
            sleep_time = 1;
        } else {
            alarm_list = alarm->link;
            now = time(NULL);

            if (alarm->time <= now) {
                sleep_time = 0;
            } else {
                sleep_time = alarm->time - now;
            }

#ifdef DEBUG
            printf("expired time: %ld, waiting: %d, message: \"%s\"\n", alarm->time,
                sleep_time, alarm->message);
#endif 
        }

        /*
         * Unlock the mutex before waiting, so that the main
         * thread can lock it to insert a new alarm request. If
         * the sleep_time is 0, then call sched_yield, giving
         * the main thread a chance to run if  it has been
         * readied by user input, without delaying the message
         * if  there's no input.
         */
        status = pthread_mutex_unlock(&alarm_mutex);
        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }

        if (sleep_time > 0) {
            sleep(sleep_time);
        } else {
            sched_yield();
        }

        /*
         * If a timer expired, print the message and free the
         * structure.
         */
        if (NULL != alarm) {
            printf("alarm exipred: (%d) %s\n", alarm->seconds, alarm->message);
            free(alarm);
        }
    }
}

int main(int argc, char *argv[])
{
    int status = 0;
    pthread_t thread;
    char line[128] = {'\0'};
    alarm_t *alarm = NULL;
    alarm_t **last = NULL;
    alarm_t *next = NULL;

    status = pthread_create(&thread, NULL, alarm_thread, NULL);
    if (0 != status) {
        err_abort(status, "Create alarm thread");
    }

    while (1) {
        int rc = 0;
        char *buf = NULL;
        alarm_t temp_alarm;

        printf("alarm> ");
        buf = fgets(line, sizeof(line), stdin);

        if (NULL == buf) {
            exit(0);
        }

        if (strlen(buf) <= 1) {
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
        status = pthread_mutex_lock(&alarm_mutex);

        if (0 != status) {
            err_abort(status, "Lock mutex");
        }

        alarm->time = time(NULL) + alarm->seconds;

        /*
         * Insert the new alarm into the list of alarms,
         * sorted by expiration time.
         */
        last = &alarm_list;
        next = *last;

        while (NULL != next) {
            if (next->time >= alarm->time) {
                alarm->link = next;
                *last = alarm;
                break;
            }

            last = &next->link;
            next = next->link;
        }

        /*
         * If we reached the end of the list, insert the new
         * alarm there.("next" is NULL, and "last" points
         * to the link field of the last item, or to the
         * list header).
         */
        if (NULL == next) {
            *last = alarm;
            alarm->link = NULL;
        }

#ifdef DEBUG
        printf("[list: ");
        for (next=alarm_list; next!=NULL; next=next->link) {
            printf("%ld(%ld)[\"%s\"] ", next->time,
                next->time - time(NULL), next->message);
        }
        printf("]\n");
#endif 

        status = pthread_mutex_unlock(&alarm_mutex);
        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }
    }

    return 0;
}
