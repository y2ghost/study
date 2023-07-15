#include "errors.h"
#include <sys/wait.h>
#include <sys/types.h>

int main(int argc, char *argv[])
{
    pid_t pid = 0;
    int seconds = 0;
    char line[128] = {'\0'};
    char message[64] = {'\0'};

    while (1) {
        int rc = 0;
        char *buf = NULL;

        printf("Alarm> ");
        buf = fgets(line, sizeof(line), stdin);

        if (NULL == buf) {
            exit(0);
        }

        if (strlen(buf) <= 1) {
            continue;
        }

        rc = sscanf(line, "%d %64[^\n]", &seconds, message);
        if (rc < 2) {
            fprintf(stderr, "Bad command\n");
            continue;
        }

        pid = fork();
        if (-1 == pid) {
            errno_abort("Fork");
        }

        if (0 == pid) {
            sleep(seconds);
            printf("(%d) %s\n", seconds, message);
            exit(0);
        } else {
            do {
                pid = waitpid(-1, NULL, WNOHANG);
                if (-1 == pid) {
                    errno_abort("Wait for child");
                }
            } while (0 != pid);
        }
    }

    return 0;
}
