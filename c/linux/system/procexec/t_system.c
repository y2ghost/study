#include "print_wait_status.h"
#include "common.h"
#include <sys/wait.h>

#define MAX_CMD_LEN 200

int main(int argc, char *argv[])
{
    char str[MAX_CMD_LEN] = {0};
    for (;;) {
        printf("Command: ");
        fflush(stdout);

        if (fgets(str, MAX_CMD_LEN, stdin) == NULL) {
            break;
        }

        int status = system(str);
        printf("system() returned: status=0x%04x (%d,%d)\n",
            (unsigned int) status, status >> 8, status & 0xff);

        if (status == -1) { 
            err_quit("system");
        } else {
            if (WIFEXITED(status) && WEXITSTATUS(status) == 127) {
                printf("(Probably) could not invoke shell\n");
            } else {
                printWaitStatus(NULL, status);
            }
        }
    }

    exit(EXIT_SUCCESS);
}
