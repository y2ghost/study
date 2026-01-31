#include "errors.h"

int main(int argc, char *argv[])
{
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

        rc = sscanf(buf, "%d %64[^\n]", &seconds, message);
        if (rc < 2) {
            fprintf(stderr, "Bad command\n");
        } else {
            sleep(seconds);
            printf("(%d) %s\n", seconds, message);
        }
    }

    return 0;
}
