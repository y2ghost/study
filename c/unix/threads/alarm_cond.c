#include "errors.h"
#include <time.h>
#include <pthread.h>

typedef struct alarm_tag {
    struct alarm_tag *link;
    int seconds;
    time_t time;
    char message[64];
} alarm_t;

static time_t current_alarm = 0;
static alarm_t *alarm_list = NULL;
static pthread_cond_t alarm_cond = PTHREAD_COND_INITIALIZER;
static pthread_mutex_t alarm_mutex = PTHREAD_MUTEX_INITIALIZER;

static void alarm_insert(alarm_t *alarm)
{
    int status = 0;
    alarm_t **last = NULL;
    alarm_t *next = NULL;

    /*
     * LOCKING PROTOCOL:
     * This routine requires that the caller have locked the
     * alarm_mutex!
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
     * If we reached the end of the list, insert the new alarm
     * there. ("next" is NULL, and "last" points to the link
     * field of the last item, or to the list header.)
     */
    if (NULL == next) {
        *last = alarm;
        alarm->link = NULL;
    }

#ifdef DEBUG
    printf("[list: ");
    for(next=alarm_list; next!=NULL; next=next->link) {
        printf("%ld(%ld)[\"%s\"] ", next->time,
            next->time-time(NULL), next->message);
    }
    printf("]\n");
#endif 

    /*
     * Wake the alarm thread if  it is not busy(that is, if 
     * current_alarm is 0, signifying that it's waiting for
     * work), or if  the new alarm comes before the one on
     * which the alarm thread is waiting.
     */
    if (0==current_alarm || alarm->time<current_alarm) {
        current_alarm = alarm->time;
        status = pthread_cond_signal(&alarm_cond);

        if (0 != status) {
            err_abort(status, "Signal cond");
        }
    }
}

static void *alarm_thread(void *arg)
{
    time_t now = 0;
    int status = 0;
    int expired = 0;
    alarm_t *alarm = NULL;
    struct timespec cond_time;

    /*
     * Loop forever, processing commands. The alarm thread will
     * be disintegrated when the process exits. Lock the mutex
     * at the start -- it will be unlocked during condition
     * waits, so the main thread can insert alarms.
     */
    status = pthread_mutex_lock(&alarm_mutex);
    if (0 != status) {
        err_abort(status, "Lock mutex");
    }

    while (1) {
        /*
         * If the alarm list is empty, wait until an alarm is
         * added. Setting current_alarm to 0 informs the insert
         * routine that the thread is not busy.
         */
        current_alarm = 0;
        while (NULL == alarm_list) {
            status = pthread_cond_wait(&alarm_cond, &alarm_mutex);

            if (0 != status) {
                err_abort(status, "Wait on cond");
            }
        }

        alarm = alarm_list;
        alarm_list = alarm->link;
        now = time(NULL);
        expired = 0;

        if (alarm->time > now) {
#ifdef DEBUG
            printf("[waiting: %ld(%ld)\"%s\"]\n", alarm->time,
                alarm->time-time(NULL), alarm->message);
#endif 
            cond_time.tv_sec = alarm->time;
            cond_time.tv_nsec = 0;
            current_alarm = alarm->time;

            while (current_alarm == alarm->time) {
                status = pthread_cond_timedwait(
                    &alarm_cond, &alarm_mutex, &cond_time);

                if (ETIMEDOUT == status) {
                    expired = 1;
                    break;
                }

                if (0 != status) {
                    err_abort(status, "Cond timedwait");
                }
            }

            if (0 == expired) {
                alarm_insert(alarm);
            }
        } else {
            expired = 1;
        }

        if (0 != expired) {
            printf("(%d) %s\n", alarm->seconds, alarm->message);
            free(alarm);
        }
    }
}

int main(int argc, char *argv[])
{
    int status = 0;
    char line[128] = {'\0'};
    alarm_t *alarm = NULL;
    pthread_t thread;

    status = pthread_create(&thread, NULL, alarm_thread, NULL);
    if (0 != status) {
        err_abort(status, "Create alarm thread");
    }

    while (1) {
        int rc = 0;
        char *buf = NULL;
        alarm_t temp_alarm;

        printf("Alarm> ");
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

        status = pthread_mutex_lock(&alarm_mutex);
        if (0 != status) {
            err_abort(status, "Lock mutex");
        }

        memcpy(alarm, &temp_alarm, sizeof(temp_alarm));
        alarm->time = time(NULL) + alarm->seconds;
        alarm_insert(alarm);
        status = pthread_mutex_unlock(&alarm_mutex);

        if (0 != status) {
            err_abort(status, "Unlock mutex");
        }
    }

    return 0;
}
