#include "ugid.h"
#include "common.h"
#include <fcntl.h>
#include <time.h>
#include <sys/stat.h>
#include <sys/acct.h>
#include <limits.h>

#define TIME_BUF_SIZE 100

/*
 * 记账记录: sudo ./acct_on pacct
 * 记账查看: ./acct_view pacct
 */
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

    printf("command  flags   term.  user     "
        "start time            CPU   elapsed\n");
    printf("                status           "
        "                      time    time\n");

    struct acct ac;
    ssize_t numRead;
    char timeBuf[TIME_BUF_SIZE] = {0};

    while ((numRead = read(acctFile, &ac, sizeof(struct acct))) > 0) {
        if (numRead != sizeof(struct acct)) {
            err_quit("partial read");
        }

        printf("%-8.8s  ", ac.ac_comm);
        printf("%c", (ac.ac_flag & AFORK) ? 'F' : '-') ;
        printf("%c", (ac.ac_flag & ASU)   ? 'S' : '-') ;
        printf("%c", (ac.ac_flag & AXSIG) ? 'X' : '-') ;
        printf(" ");
        printf("%c", (ac.ac_flag & ACORE) ? 'C' : '-') ;
        printf(" ");
        printf(" %#6lx   ", (unsigned long) ac.ac_exitcode);
        printf("          ");
        char *s = get_name_byuid(ac.ac_uid);
        printf("%-8.8s ", (s == NULL) ? "???" : s);
        time_t t = ac.ac_btime;
        struct tm *loc = localtime(&t);

        if (loc == NULL) {
            printf("???Unknown time???  ");
        } else {
            strftime(timeBuf, TIME_BUF_SIZE, "%Y-%m-%d %T ", loc);
            printf("%s ", timeBuf);
        }

        printf("%5.2f %7.2f ", (double) (comptToLL(ac.ac_utime) +
            comptToLL(ac.ac_stime)) / sysconf(_SC_CLK_TCK),
            (double) comptToLL(ac.ac_etime) / sysconf(_SC_CLK_TCK));
        printf("\n");
    }

    if (numRead == -1) {
        err_quit("read");
    }

    exit(EXIT_SUCCESS);
}
