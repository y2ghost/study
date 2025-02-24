#include <sys/capability.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <errno.h>
#include <string.h>
#include <fcntl.h>

#define err_sys(msg)    do { perror(msg); exit(EXIT_FAILURE); \
                        } while (0)

int main(int argc, char *argv[])
{
    cap_t caps = cap_get_proc();
    if (caps == NULL) {
        err_sys("cap_get_proc");
    }

    char *str = cap_to_text(caps, NULL);
    if (str == NULL) {
        err_sys("cap_to_text");
    }

    printf("Capabilities: %s\n", str);
    cap_free(caps);
    cap_free(str);

    if (argc > 1) {
        int fd = open(argv[1], O_RDONLY);
        if (fd >= 0) {
            printf("Successfully opened %s\n", argv[1]);
        } else {
            printf("Open failed: %s\n", strerror(errno));
        }
    }

    exit(EXIT_SUCCESS);
}
