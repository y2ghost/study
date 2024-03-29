#include "print_rusage.h"
#include "common.h"
#include <sys/resource.h>

void printRusage(const char *leader, const struct rusage *ru)
{
    const char *ldr = (leader == NULL) ? "" : leader;
    printf("%sCPU time (secs):         user=%.3f; system=%.3f\n", ldr,
        ru->ru_utime.tv_sec + ru->ru_utime.tv_usec / 1000000.0,
        ru->ru_stime.tv_sec + ru->ru_stime.tv_usec / 1000000.0);
    printf("%sMax resident set size:   %ld\n", ldr, ru->ru_maxrss);
    printf("%sIntegral shared memory:  %ld\n", ldr, ru->ru_ixrss);
    printf("%sIntegral unshared data:  %ld\n", ldr, ru->ru_idrss);
    printf("%sIntegral unshared stack: %ld\n", ldr, ru->ru_isrss);
    printf("%sPage reclaims:           %ld\n", ldr, ru->ru_minflt);
    printf("%sPage faults:             %ld\n", ldr, ru->ru_majflt);
    printf("%sSwaps:                   %ld\n", ldr, ru->ru_nswap);
    printf("%sBlock I/Os:              input=%ld; output=%ld\n",
        ldr, ru->ru_inblock, ru->ru_oublock);
    printf("%sSignals received:        %ld\n", ldr, ru->ru_nsignals);
    printf("%sIPC messages:            sent=%ld; received=%ld\n",
        ldr, ru->ru_msgsnd, ru->ru_msgrcv);
    printf("%sContext switches:        voluntary=%ld; "
        "involuntary=%ld\n", ldr, ru->ru_nvcsw, ru->ru_nivcsw);
}
