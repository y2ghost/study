#include "common.h"
#include <stdlib.h>

static void atexitFunc1(void)
{
    printf("atexit function 1 called\n");
}

static void atexitFunc2(void)
{
    printf("atexit function 2 called\n");
}

// on_exit函数不具备移植性，此处仅示例
static void onexitFunc(int exitStatus, void *arg)
{
    printf("on_exit function called: status=%d, arg=%ld\n",
        exitStatus, (long) arg);
}

int main(int argc, char *argv[])
{
    if (on_exit(onexitFunc, (void *) 10) != 0) {
        err_quit("on_exit 1");
    }

    if (atexit(atexitFunc1) != 0) {
        err_quit("atexit 1");
    }

    if (atexit(atexitFunc2) != 0) {
        err_quit("atexit 2");
    }

    if (on_exit(onexitFunc, (void *) 20) != 0) {
        err_quit("on_exit 2");
    }

    exit(2);
}

