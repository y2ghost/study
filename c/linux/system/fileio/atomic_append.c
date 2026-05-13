#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>
#include <stdbool.h>

/*
 * 原子地向文件末尾追加数据
 */
int main(int argc, char *argv[])
{
    if (argc < 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file num-bytes [x]\n"
                 "        'x' means use lseek() instead of O_APPEND\n",
                 argv[0]);
    }

    bool useLseek = argc > 3;
    int flags = useLseek ? 0 : O_APPEND;
    int numBytes = atoi(argv[2]);

    int fd = open(argv[1], O_RDWR | O_CREAT | flags, S_IRUSR | S_IWUSR);
    if (fd == -1) {
        err_quit("open");
    }

    for (int j = 0; j < numBytes; j++) {
        if (useLseek) {
            if (lseek(fd, 0, SEEK_END) == -1) {
                err_quit("lseek");
            }
        }

        if (write(fd, "x", 1) != 1) {
            err_quit("write() failed");
        }
    }

    printf("%ld done\n", (long) getpid());
    exit(EXIT_SUCCESS);
}
