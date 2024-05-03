#include "common.h"
#include <sys/resource.h>

static struct {
    int rm_type;
    const char *rm_name;
} limits[] = {
    {RLIMIT_AS,          "RLIMIT_AS"},
    {RLIMIT_CORE,        "RLIMIT_CORE"},
    {RLIMIT_CPU,         "RLIMIT_CPU"},
    {RLIMIT_DATA,        "RLIMIT_DATA"},
    {RLIMIT_FSIZE,       "RLIMIT_FSIZE"},
    {RLIMIT_MEMLOCK,     "RLIMIT_MEMLOCK"},
    {RLIMIT_NOFILE,      "RLIMIT_NOFILE"},
    {RLIMIT_NPROC,       "RLIMIT_NPROC"},
    {RLIMIT_RSS,         "RLIMIT_RSS"},
    {RLIMIT_STACK,       "RLIMIT_STACK"},
    {-1, ""},
};

static void pr_limits(const char *, int);

int main(void)
{
    int i = 0;
    int rm_type = 0;
    const char *rm_name = NULL;

    i = 0;
    while (1) {
        rm_type = limits[i].rm_type;
        if (-1 == rm_type) {
            break;
        }

        rm_name = limits[i].rm_name;
        pr_limits(rm_name, rm_type);
        i++;
    }

    return 0;
}

static void pr_limits(const char *name, int resource)
{
    struct rlimit limit;
    unsigned long long lim = 0;

    if (getrlimit(resource, &limit) < 0) {
        err_sys("getrlimit error for %s", name);
    }

    printf("%-14s  ", name);
    if (RLIM_INFINITY == limit.rlim_cur) {
        printf("(infinite)  ");
    } else {
        lim = limit.rlim_cur;
        printf("%10lld  ", lim);
    }

    if (RLIM_INFINITY == limit.rlim_max) {
        printf("(infinite)");
    } else {
        lim = limit.rlim_max;
        printf("%10lld", lim);
    }

    putchar((int)'\n');
}
