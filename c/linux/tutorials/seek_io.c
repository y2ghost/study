#include <sys/stat.h>
#include <fcntl.h>
#include <ctype.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

int main(int ac, char **av)
{
    int j = 0;
    int ap = 0;
    int fd = 0;
    size_t len = 0;
    off_t offset = 0;
    char *buf = NULL;
    ssize_t read_bytes = 0;
    ssize_t writ_bytes = 0;

    if (ac<3 || 0==strcmp(av[1], "--help")) {
        fprintf(stderr, "%s file {r<len>|R<len>|w<str>|s<offset>}\n", av[0]);
        return -1;
    }

    fd = open(av[1], O_RDWR|O_CREAT,
        S_IRUSR|S_IWUSR|S_IRGRP|S_IWGRP|S_IROTH|S_IWOTH);
    if (-1 == fd) {
        return -1;
    }

    for (ap=2; ap<ac; ++ap) {
        switch (av[ap][0]) {
        case 'r':
        case 'R':
            len = atol(&av[ap][1]);
            buf = malloc(len);

            read_bytes = read(fd, buf, len);
            if (read_bytes > 0) {
                printf("%s: ", av[ap]);
                for (j=0; j<read_bytes; ++j) {
                    if ('r'==av[ap][0]) {
                        printf("%c", buf[j]);
                    } else {
                        printf("%02x ", (unsigned int)buf[j]);
                    }
                }

                printf("\n");
            }

            free(buf);
            break;
        case 'w':
            writ_bytes = write(fd, &av[ap][1], strlen(&av[ap][1]));
            if (-1 == writ_bytes) {
                fprintf(stderr, "write error!\n");
                return -1;
            }

            printf("%s: wrote %ld bytes\n", av[ap], (long)writ_bytes);
            break;
        case 's':
            offset = atol(&av[ap][1]);
            if (-1 == lseek(fd, offset, SEEK_SET)) {
                fprintf(stderr, "lseek error!\n");
                return -1;
            }

            printf("%s: seek succeeded\n", av[ap]);
            break;
        default:
            fprintf(stderr, "Argument must start with [rRws]: %s\n", av[ap]);
        }
    }

    return 0;
}
