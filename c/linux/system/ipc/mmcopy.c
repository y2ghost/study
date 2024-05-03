#include "common.h"
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    if (argc != 3) {
        err_quit("%s source-file dest-file\n", argv[0]);
    }

    int fdSrc = open(argv[1], O_RDONLY);
    if (fdSrc == -1) {
        err_sys("open");
    }

    struct stat sb;
    if (fstat(fdSrc, &sb) == -1) {
        err_sys("fstat");
    }

    if (sb.st_size == 0) {
        exit(EXIT_SUCCESS);
    }

    char *src = mmap(NULL, sb.st_size, PROT_READ, MAP_PRIVATE, fdSrc, 0);
    if (src == MAP_FAILED) {
        err_sys("mmap");
    }

    int fdDst = open(argv[2], O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
    if (fdDst == -1) {
        err_sys("open");
    }

    if (ftruncate(fdDst, sb.st_size) == -1) {
        err_sys("ftruncate");
    }

    char *dst = mmap(NULL, sb.st_size, PROT_READ | PROT_WRITE, MAP_SHARED, fdDst, 0);
    if (dst == MAP_FAILED) {
        err_sys("mmap");
    }

    memcpy(dst, src, sb.st_size);
    if (msync(dst, sb.st_size, MS_SYNC) == -1) {
        err_sys("msync");
    }

    exit(EXIT_SUCCESS);
}

