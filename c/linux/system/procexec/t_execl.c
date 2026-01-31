#include "common.h"
#include <stdlib.h>

int main(int argc, char *argv[])
{
    printf("Initial value of USER: %s\n", getenv("USER"));
    if (putenv("USER=britta") != 0) {
        err_quit("putenv");
    }

    execl("/usr/bin/printenv", "printenv", "USER", "SHELL", (char *) NULL);
    err_quit("execl");
}

