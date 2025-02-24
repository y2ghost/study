#include "print_wait_status.h"
#include "common.h"
#include <ctype.h>
#include <limits.h>

#define POPEN_FMT "/bin/ls -d %s 2> /dev/null"
#define PAT_SIZE 50
#define PCMD_BUF_SIZE (sizeof(POPEN_FMT) + PAT_SIZE)

int main(int argc, char *argv[])
{
    char pat[PAT_SIZE] = {0};
    char popenCmd[PCMD_BUF_SIZE] = {0};
    char pathname[PATH_MAX] = {0};

    for (;;) {
        printf("pattern: ");
        fflush(stdout);

        if (fgets(pat, PAT_SIZE, stdin) == NULL) {
            break;
        }

        int len = strlen(pat);
        if (len <= 1) {
            continue;
        }

        if (pat[len - 1] == '\n') {
            pat[len - 1] = '\0';
        }

        int badPattern = 0;
        int j = 0;

        for (j = 0; j < len && !badPattern; j++) {
            if (!isalnum((unsigned char) pat[j]) &&
                    strchr("_*?[^-].", pat[j]) == NULL) {
                badPattern = 1;
            }
        }

        if (badPattern) {
            printf("Bad pattern character: %c\n", pat[j - 1]);
            continue;
        }

        snprintf(popenCmd, PCMD_BUF_SIZE, POPEN_FMT, pat);
        FILE *fp = popen(popenCmd, "r");

        if (fp == NULL) {
            printf("popen() failed\n");
            continue;
        }

        int fileCnt = 0;
        while (fgets(pathname, PATH_MAX, fp) != NULL) {
            printf("%s", pathname);
            fileCnt++;
        }

        int status = pclose(fp);
        printf("    %d matching file%s\n", fileCnt, (fileCnt != 1) ? "s" : "");
        printf("    pclose() status = %#x\n", (unsigned int) status);

        if (status != -1) {
            printWaitStatus("\t", status);
        }
    }

    exit(EXIT_SUCCESS);
}

