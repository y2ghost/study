#include <sys/capability.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

#define err_sys(msg)    do { perror(msg); exit(EXIT_FAILURE); \
                        } while (0)

int main(int argc, char *argv[])
{
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <pid>\n", argv[0]);
        exit(EXIT_FAILURE);
    }

    cap_t caps = cap_get_pid(atoi(argv[1]));
    if (caps == NULL) {
        err_sys("cap_get_pid");
    }

    char *str = cap_to_text(caps, NULL);
    if (str == NULL) {
        err_sys("cap_to_text");
    }

    printf("Capabilities: %s\n", str);
    cap_free(caps);
    cap_free(str);
    exit(EXIT_SUCCESS);
}
