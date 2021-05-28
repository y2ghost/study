#include "apue.h"

unsigned int sleep2(unsigned int);
static void sig_int(int);

int main(void)
{
    unsigned int unslept;

    if (SIG_ERR == signal(SIGINT, sig_int)) {
        err_sys("signal(SIGINT) error");
    }

    unslept = sleep2(5);
    printf("sleep2 returned: %u\n", unslept);
    return 0;
}

static void sig_int(int signo)
{
    int i = 0;
    int j = 0;
    volatile int k = 0;

    printf("\nsig_int starting\n");
    for (i=0; i<300000; i++) {
        for (j=0; j<4000; j++) {
            k += i * j;
        }
    }

    printf("sig_int finished\n");
}
