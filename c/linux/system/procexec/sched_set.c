#include "common.h"
#include <sched.h>

/*
 * 演示进程优先级示例
 * 操作示例
 *   su
 *   sleep 100 &
 *   ./sched_view <sleep PID>
 *   ./sched_set f 25 <sleep PID>
 *   ./sched_view <sleeep PID>
 *
 */
int main(int argc, char *argv[])
{
    if (argc < 3 || strchr("rfobi" , argv[1][0]) == NULL) {
        err_quit("%s policy priority [pid...]\n"
            "    policy is 'r' (RR), 'f' (FIFO), "
            "'b' (BATCH), 'i' (IDLE), or 'o' (OTHER)\n",
            argv[0]);
    }

    int pol = (argv[1][0] == 'r') ? SCHED_RR :
        (argv[1][0] == 'f') ? SCHED_FIFO :
        (argv[1][0] == 'b') ? SCHED_BATCH :
        (argv[1][0] == 'i') ? SCHED_IDLE :
        SCHED_OTHER;
    struct sched_param sp;
    sp.sched_priority = atoi(argv[2]);

    for (int j = 3; j < argc; j++) {
        if (sched_setscheduler(atol(argv[j]), pol, &sp) == -1) {
            err_sys("sched_setscheduler");
        }
    }

    exit(EXIT_SUCCESS);
}
