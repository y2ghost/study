#include "print_rlimit.h"
#include "common.h"
#include <sys/resource.h>

int printRlimit(const char *msg, int resource)
{
    struct rlimit rlim;
    if (getrlimit(resource, &rlim) == -1) {
        return -1;
    }

    printf("%s soft=", msg);
    if (rlim.rlim_cur == RLIM_INFINITY) {
        printf("infinite");
    } else if (rlim.rlim_cur == RLIM_SAVED_CUR) {
        printf("unrepresentable");
    } else {
        printf("%lld", (long long) rlim.rlim_cur);
    }

    printf("; hard=");
    if (rlim.rlim_max == RLIM_INFINITY) {
        printf("infinite\n");
    } else if (rlim.rlim_max == RLIM_SAVED_MAX) {
        printf("unrepresentable");
    } else {
        printf("%lld\n", (long long) rlim.rlim_max);
    }

    return 0;
}

