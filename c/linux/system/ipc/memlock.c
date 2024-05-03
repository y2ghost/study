#include "common.h"
#include <sys/mman.h>

static void displayMincore(char *addr, size_t length)
{
    long pageSize = sysconf(_SC_PAGESIZE);
    long numPages = (length + pageSize - 1) / pageSize;
    unsigned char *vec = malloc(numPages);

    if (vec == NULL) {
        err_sys("malloc");
    }

    if (mincore(addr, length, vec) == -1) {
        err_sys("mincore");
    }

    for (long j = 0; j < numPages; j++) {
        if (j % 64 == 0) {
            printf("%s%10p: ", (j == 0) ? "" : "\n", addr + (j * pageSize));
        }

        printf("%c", (vec[j] & 1) ? '*' : '.');
    }

    printf("\n");
    free(vec);
}

// 执行示例: ./memlock 32 8 3
int main(int argc, char *argv[])
{
    if (argc != 4 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s num-pages lock-page-step lock-page-len\n", argv[0]);
    }

    long pageSize = sysconf(_SC_PAGESIZE);
    if (pageSize == -1) {
        err_sys("sysconf(_SC_PAGESIZE)");
    }

    size_t len = atoi(argv[1]) * pageSize;
    long stepSize = atoi(argv[2]) * pageSize;
    size_t lockLen = atoi(argv[3]) * pageSize;
    char *addr = mmap(NULL, len, PROT_READ, MAP_SHARED | MAP_ANONYMOUS, -1, 0);

    if (addr == MAP_FAILED) {
        err_sys("mmap");
    }

    printf("Allocated %ld (%#lx) bytes starting at %p\n",
        (long) len, (unsigned long) len, addr);
    printf("Before mlock:\n");
    displayMincore(addr, len);

    for (long j = 0; j + lockLen <= len; j += stepSize) {
        if (mlock(addr + j, lockLen) == -1) {
            err_sys("mlock");
        }
    }

    printf("After mlock:\n");
    displayMincore(addr, len);
    exit(EXIT_SUCCESS);
}
