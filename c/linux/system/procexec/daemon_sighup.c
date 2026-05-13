#include "become_daemon.h"
#include "common.h"
#include <sys/stat.h>
#include <signal.h>
#include <time.h>
#include <stdarg.h>

#define SBUF_SIZE   100
#define TS_BUF_SIZE sizeof("YYYY-MM-DD HH:MM:SS")

static const char *LOG_FILE = "/tmp/ds.log";
static const char *CONFIG_FILE = "/tmp/ds.conf";
static FILE *logfp = NULL;

static void logMessage(const char *format, ...)
{
    va_list argList;
    const char *TIMESTAMP_FMT = "%F %X";
    char timestamp[TS_BUF_SIZE] = {0};
    time_t t = time(NULL);
    struct tm *loc = localtime(&t);

    if (loc == NULL ||
        strftime(timestamp, TS_BUF_SIZE, TIMESTAMP_FMT, loc) == 0) {
        fprintf(logfp, "???Unknown time????: ");
    } else {
        fprintf(logfp, "%s: ", timestamp);
    }

    va_start(argList, format);
    vfprintf(logfp, format, argList);
    fprintf(logfp, "\n");
    va_end(argList);
}

static void logOpen(const char *logFilename)
{
    mode_t m = umask(077);
    logfp = fopen(logFilename, "a");
    umask(m);

    if (logfp == NULL) {
        exit(EXIT_FAILURE);
    }

    setbuf(logfp, NULL);
    logMessage("Opened log file");
}

static void logClose(void)
{
    logMessage("Closing log file");
    fclose(logfp);
}

static void readConfigFile(const char *configFilename)
{
    char str[SBUF_SIZE] = {0};
    FILE *configfp = fopen(configFilename, "r");

    if (configfp != NULL) {
        if (fgets(str, SBUF_SIZE, configfp) == NULL) {
            str[0] = '\0';
        } else {
            str[strlen(str) - 1] = '\0';
        }

        logMessage("Read config file: %s", str);
        fclose(configfp);
    }
}

static volatile sig_atomic_t hupReceived = 0;

static void sighupHandler(int sig)
{
    hupReceived = 1;
}

/*
 * 执行说明
 * echo START > /tmp/ds.conf
 * ./daemon_sighup
 * cat /tmp/ds.log
 * echo CHANGED > /tmp/ds.conf
 * date +'%F %X'; mv /tmp/ds.log /tmp/old_ds.log
 * date +'%F %X'; killall -HUP daemon_sighup
 * cat /tmp/ds.log
 * cat /tmp/old_ds.log
 * killall daemon_sighup
 */
int main(int argc, char *argv[])
{
    const int SLEEP_TIME = 15;
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    sa.sa_handler = sighupHandler;

    if (sigaction(SIGHUP, &sa, NULL) == -1) {
        err_sys("sigaction");
    }

    if (becomeDaemon(0) == -1) {
        err_sys("becomeDaemon");
    }

    logOpen(LOG_FILE);
    readConfigFile(CONFIG_FILE);
    int unslept = SLEEP_TIME;
    int count = 0;

    for (;;) {
        unslept = sleep(unslept);
        if (hupReceived) {
            hupReceived = 0;
            logClose();
            logOpen(LOG_FILE);
            readConfigFile(CONFIG_FILE);
        }

        if (unslept == 0) {
            count++;
            logMessage("Main: %d", count);
            unslept = SLEEP_TIME;
        }
    }
}
