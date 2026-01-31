#include "common.h"
#include <fcntl.h>
#include <sys/mman.h>

/* 1 GB */
#define COPYINCR (1024*1024*1024)

int main(int argc, char *argv[])
{
    int fdin = 0;
    int fdout = 0;
    void *src = NULL;
    void *dst = NULL;
    size_t copysz = 0;
    struct stat sbuf;
    off_t fsz = 0;

    if (3 != argc) {
        err_quit("usage: %s <fromfile> <tofile>", argv[0]);
    }

    fdin = open(argv[1], O_RDONLY);
    if (fdin < 0) {
        err_sys("can't open %s for reading", argv[1]);
    }

    fdout = open(argv[2], O_RDWR | O_CREAT | O_TRUNC, FILE_MODE);
    if (fdout < 0) {
        err_sys("can't creat %s for writing", argv[2]);
    }

    if (fstat(fdin, &sbuf) < 0) {
        err_sys("fstat error");
    }

    if (ftruncate(fdout, sbuf.st_size) < 0) {
        err_sys("ftruncate error");
    }

    while (fsz < sbuf.st_size) {
        if ((sbuf.st_size-fsz) > COPYINCR) {
            copysz = COPYINCR;
        } else {
            copysz = sbuf.st_size - fsz;
        }

        src = mmap(0, copysz, PROT_READ, MAP_SHARED, fdin, fsz);
        if (MAP_FAILED == src) {
            err_sys("mmap error for input");
        }

        dst = mmap(0, copysz, PROT_READ | PROT_WRITE, MAP_SHARED, fdout, fsz);
        if (MAP_FAILED == dst) {
            err_sys("mmap error for output");
        }

        memcpy(dst, src, copysz);
        munmap(src, copysz);
        munmap(dst, copysz);
        fsz += copysz;
    }

    return 0;
}
