#include "common.h"
#include <ctype.h>
#include <fcntl.h>

#define BSZ 4096

unsigned char buf[BSZ] = {'\0'};

unsigned char translate(unsigned char c)
{
    if (isalpha(c)) {
        if (c >= 'n') {
            c -= 13;
        } else if (c >= 'a') {
            c += 13;
        } else if (c >= 'N') {
            c -= 13;
        } else {
            c += 13;
        }
    }

    return c;
}

int main(int argc, char* argv[])
{
    int i = 0;
    int n = 0;
    int nw = 0;
    int ifd = 0;
    int ofd = 0;

    if (3 != argc) {
        err_quit("usage: rot13 infile outfile");
    }

    ifd = open(argv[1], O_RDONLY);
    if (ifd < 0) {
        err_sys("can't open %s", argv[1]);
    }

    ofd = open(argv[2], O_RDWR|O_CREAT|O_TRUNC, FILE_MODE);
    if (ofd < 0) {
        err_sys("can't create %s", argv[2]);
    }

    while (1) {
        n = read(ifd, buf, BSZ);
        if (n <= 0) {
            break;
        }

        for (i=0; i<n; i++) {
            buf[i] = translate(buf[i]);
        }

        nw = write(ofd, buf, n);
        if (nw != n) {
            if (nw < 0) {
                err_sys("write failed");
            } else {
                err_quit("short write (%d/%d)", nw, n);
            }
        }
    }

    fsync(ofd);
    return 0;
}
