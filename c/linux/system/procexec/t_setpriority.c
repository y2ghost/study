#include "common.h"
#include <sys/time.h>
#include <sys/resource.h>
#include <errno.h>

int main(int argc, char *argv[])
{
    if (argc != 4 || strchr("pgu", argv[1][0]) == NULL) {
        err_quit("%s {p|g|u} who priority\n"
            "    set priority of: p=process; g=process group; "
            "u=processes for user\n", argv[0]);
    }

    int which = (argv[1][0] == 'p') ? PRIO_PROCESS :
        (argv[1][0] == 'g') ? PRIO_PGRP : PRIO_USER;
    id_t who = atol(argv[2]);
    int prio = atoi(argv[3]);

    if (setpriority(which, who, prio) == -1) {
        err_sys("setpriority");
    }

    errno = 0;
    prio = getpriority(which, who);

    if (prio == -1 && errno != 0) {
        err_sys("getpriority");
    }

    printf("Nice value = %d\n", prio);
    exit(EXIT_SUCCESS);
}

