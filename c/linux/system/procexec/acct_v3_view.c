#include "ugid.h"
#include "common.h"
#include <fcntl.h>
#include <time.h>
#include <sys/stat.h>
#include <sys/acct.h>
#include <limits.h>

#define TIME_BUF_SIZE 100

// 执行示例: ./acct_v3_view pacct
static long long comptToLL(comp_t ct)
{
    const int EXP_SIZE = 3;
    const int MANTISSA_SIZE = 13;
    const int MANTISSA_MASK = (1 << MANTISSA_SIZE) - 1;
    long long mantissa = ct & MANTISSA_MASK;
    long long exp = (ct >> MANTISSA_SIZE) & ((1 << EXP_SIZE) - 1);
    return mantissa << (exp * 3);
}

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file\n", argv[0]);
    }

    int acctFile = open(argv[1], O_RDONLY);
    if (acctFile == -1) {
        err_quit("open");
    }

    printf("ver. command    flags   term.   PID   PPID  user     group"
        "      start date+time     CPU   elapsed\n");
    printf("                       status                             "
        "                          time    time\n");

    ssize_t numRead = 0; 
    struct acct_v3 ac;

    while ((numRead = read(acctFile, &ac, sizeof(struct acct_v3))) > 0) {
        if (numRead != sizeof(struct acct_v3)) {
            err_quit("partial read");
        }

        printf("%1d    ", (int) ac.ac_version);
        printf("%-8.8s   ", ac.ac_comm);
        printf("%c", (ac.ac_flag & AFORK) ? 'F' : '-') ;
        printf("%c", (ac.ac_flag & ASU)   ? 'S' : '-') ;
        printf("%c", (ac.ac_flag & AXSIG) ? 'X' : '-') ;
        printf("%c", (ac.ac_flag & ACORE) ? 'C' : '-') ;
        printf(" %#6lx   ", (unsigned long) ac.ac_exitcode);
        printf(" %5ld %5ld  ", (long) ac.ac_pid, (long) ac.ac_ppid);
        char *s = get_name_byuid(ac.ac_uid);
        printf("%-8.8s ", (s == NULL) ? "???" : s);
        s = get_gname_bygid(ac.ac_gid);
        printf("%-8.8s ", (s == NULL) ? "???" : s);
        time_t t = ac.ac_btime;
        struct tm *loc = localtime(&t);

        if (loc == NULL) {
            printf("???Unknown time???  ");
        } else {
            char timeBuf[TIME_BUF_SIZE];
            strftime(timeBuf, TIME_BUF_SIZE, "%Y-%m-%d %T ", loc);
            printf("%s ", timeBuf);
        }

        printf("%5.2f %7.2f ", (double) (comptToLL(ac.ac_utime) +
            comptToLL(ac.ac_stime)) / sysconf(_SC_CLK_TCK),
            ac.ac_etime / sysconf(_SC_CLK_TCK));
        printf("\n");
    }

    if (numRead == -1) {
        err_quit("read");
    }

    exit(EXIT_SUCCESS);
}

