#include "common.h"
#include <sys/mman.h>

#define LEN (1024 * 1024)
#define SHELL_FMT "cat /proc/%ld/maps | grep zero"
#define CMD_SIZE (sizeof(SHELL_FMT) + 20)

int main(int argc, char *argv[])
{
    char *addr = mmap(NULL, LEN, PROT_NONE, MAP_SHARED | MAP_ANONYMOUS, -1, 0);
    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    printf("Before mprotect()\n");
    char cmd[CMD_SIZE] = {0};
    snprintf(cmd, CMD_SIZE, SHELL_FMT, (long) getpid());
    system(cmd);

    if (mprotect(addr, LEN, PROT_READ | PROT_WRITE) == -1) {
        err_sys("mprotect");
    }

    printf("After mprotect()\n");
    system(cmd);
    exit(EXIT_SUCCESS);
}

