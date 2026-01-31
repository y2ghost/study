#include "common.h"
#include <sys/wait.h>
#include <sys/mman.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    int *addr = mmap(NULL, sizeof(int), PROT_READ | PROT_WRITE,
        MAP_SHARED | MAP_ANONYMOUS, -1, 0);
    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }


    *addr = 1;
    switch (fork()) {
    case -1:
        err_sys("fork");
    case 0:
        printf("Child started, value = %d\n", *addr);
        (*addr)++;

        if (munmap(addr, sizeof(int)) == -1) {
            err_sys("munmap");
        }

        exit(EXIT_SUCCESS);
    default:
        if (wait(NULL) == -1) {
            err_sys("wait");
        }

        printf("In parent, value = %d\n", *addr);
        if (munmap(addr, sizeof(int)) == -1) {
            err_sys("munmap");
        }

        exit(EXIT_SUCCESS);
    }
}
