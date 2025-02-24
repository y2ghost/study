#include "common.h"
#include <time.h>
#include <utmpx.h>
#include <paths.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [utmp-pathname]\n", argv[0]);
    }

    if (argc > 1) {
        if (utmpxname(argv[1]) == -1) {
            err_sys("utmpxname");
        }
    }

    setutxent();
    printf("user     type            PID line   id  host     ");
    printf("term exit session  address         date/time\n");
    struct utmpx *ut = NULL;
    struct in_addr in;

    while ((ut = getutxent()) != NULL) {
        printf("%-8s ", ut->ut_user);
        printf("%-9.9s ",
            (ut->ut_type == EMPTY) ?         "EMPTY" :
            (ut->ut_type == RUN_LVL) ?       "RUN_LVL" :
            (ut->ut_type == BOOT_TIME) ?     "BOOT_TIME" :
            (ut->ut_type == NEW_TIME) ?      "NEW_TIME" :
            (ut->ut_type == OLD_TIME) ?      "OLD_TIME" :
            (ut->ut_type == INIT_PROCESS) ?  "INIT_PR" :
            (ut->ut_type == LOGIN_PROCESS) ? "LOGIN_PR" :
            (ut->ut_type == USER_PROCESS) ?  "USER_PR" :
            (ut->ut_type == DEAD_PROCESS) ?  "DEAD_PR" : "???");
        printf("(%1d) ", ut->ut_type);
        printf("%5ld %-6.6s %-3.5s %-9.9s ", (long) ut->ut_pid,
            ut->ut_line, ut->ut_id, ut->ut_host);
        printf("%3d %3d ", ut->ut_exit.e_termination, ut->ut_exit.e_exit);
        printf("%8ld ", (long) ut->ut_session);

        in.s_addr = ut->ut_addr_v6[0];
        char host_info[INET6_ADDRSTRLEN] = {0};
        inet_ntop(AF_INET, &in, host_info, sizeof(host_info));
        printf(" %-15.15s ", host_info);
        time_t tv_sec = ut->ut_tv.tv_sec;
        printf("%s", ctime((time_t *) &tv_sec));
    }

    endutxent();
    exit(EXIT_SUCCESS);
}
