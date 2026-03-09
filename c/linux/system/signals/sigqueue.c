#include "common.h"
#include <signal.h>

int main(int argc, char *argv[])
{
    if (argc < 4 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pid sig-num data [num-sigs]\n", argv[0]);
    }

    printf("%s: PID is %ld, UID is %ld\n", argv[0],
            (long) getpid(), (long) getuid());
    int sig = atoi(argv[2]);
    int sigData = atoi(argv[3]);
    int numSigs = (argc > 4) ? atoi(argv[4]) : 1;
    union sigval sv = {0};

    for (int j = 0; j < numSigs; j++) {
        sv.sival_int = sigData + j;
        if (sigqueue(atol(argv[1]), sig, sv) == -1) {
            err_quit("sigqueue %d", j);
        }
    }

    exit(EXIT_SUCCESS);
}
