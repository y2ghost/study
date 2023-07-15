#include "common.h"
#include <signal.h>

int main(int argc, char *argv[])
{
    if (argc < 4 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pid num-sigs sig-num [sig-num-2]\n", argv[0]);
    }

    pid_t pid = atol(argv[1]);
    int numSigs = atoi(argv[2]);
    int sig = atoi(argv[3]);
    printf("%s: sending signal %d to process %ld %d times\n",
            argv[0], sig, (long) pid, numSigs);

    for (int j = 0; j < numSigs; j++) {
        if (kill(pid, sig) == -1) {
            err_quit("kill");
        }
    }

    if (argc > 4) {
        if (kill(pid, atoi(argv[4])) == -1) {
            err_quit("kill");
        }
    }

    printf("%s: exiting\n", argv[0]);
    exit(EXIT_SUCCESS);
}

