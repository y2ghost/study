#include "common.h"
#include <time.h>
#include <utmpx.h>
#include <paths.h>

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s username [sleep-time]\n", argv[0]);
    }

    struct utmpx ut;
    memset(&ut, 0, sizeof(struct utmpx));
    ut.ut_type = USER_PROCESS;
    strncpy(ut.ut_user, argv[1], sizeof(ut.ut_user));

    if (time((time_t *) &ut.ut_tv.tv_sec) == -1) {
        err_sys("time");
    }

    ut.ut_pid = getpid();
    char *devName = ttyname(STDIN_FILENO);

    if (devName == NULL) {
        err_sys("ttyname");
    }

    if (strlen(devName) <= 8) {
        err_quit("Terminal name is too short: %s", devName);
    }

    strncpy(ut.ut_line, devName + 5, sizeof(ut.ut_line));
    strncpy(ut.ut_id, devName + 8, sizeof(ut.ut_id));
    printf("Creating login entries in utmp and wtmp\n");
    printf("        using pid %ld, line %.*s, id %.*s\n",
        (long) ut.ut_pid, (int) sizeof(ut.ut_line), ut.ut_line,
        (int) sizeof(ut.ut_id), ut.ut_id);
    setutxent();

    if (pututxline(&ut) == NULL) {
        err_sys("pututxline");
    }

    updwtmpx(_PATH_WTMP, &ut);
    sleep((argc > 2) ? atoi(argv[2]) : 15);
    ut.ut_type = DEAD_PROCESS;
    time((time_t *) &ut.ut_tv.tv_sec);
    memset(&ut.ut_user, 0, sizeof(ut.ut_user));
    printf("Creating logout entries in utmp and wtmp\n");
    setutxent();

    if (pututxline(&ut) == NULL) {
        err_sys("pututxline");
    }

    updwtmpx(_PATH_WTMP, &ut);
    endutxent();
    exit(EXIT_SUCCESS);
}

