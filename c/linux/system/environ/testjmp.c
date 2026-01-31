#include "common.h"
#include <setjmp.h>

static void f1(int, int, int, int);
static void f2(void);

static int globval = 0;
static jmp_buf jmpbuffer;

int main(void)
{
    int autoval = 0;
    register int regival = 0;
    volatile int volaval = 0;
    static int statval = 0;

    globval = 1;
    autoval = 2;
    regival = 3;
    volaval = 4;
    statval = 5;

    if (0 != setjmp(jmpbuffer)) {
        printf("after longjmp:\n");
        printf("globval = %d, autoval = %d, regival = %d,"
            " volaval = %d, statval = %d\n",
            globval, autoval, regival, volaval, statval);
        exit(0);
    }

    /* Change variables after setjmp, but before longjmp  */
    globval = 95;
    autoval = 96;
    regival = 97;
    volaval = 98;
    statval = 99;
    f1(autoval, regival, volaval, statval);
    return 0;
}

static void f1(int i, int j, int k, int l)
{
    printf("in f1():\n");
    printf("globval = %d, autoval = %d, regival = %d,"
        " volaval = %d, statval = %d\n", globval, i, j, k, l);
    f2();
}

static void f2(void)
{
    longjmp(jmpbuffer, 1);
}
