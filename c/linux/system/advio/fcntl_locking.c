#include "common.h"
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>

#define MAX_LINE 100

static int mandLockingEnabled(int fd)
{
    struct stat sb;
    if (fstat(fd, &sb) == -1) {
        err_sys("stat");
    }

    return (sb.st_mode & S_ISGID) != 0 && (sb.st_mode & S_IXGRP) == 0;
}

static void displayCmdFmt(int argc, char *argv[], const int fdList[])
{
    if (argc == 2) {
        printf("\nFormat: cmd lock start length [whence]\n\n");
    } else {
        printf("\nFormat: %scmd lock start length [whence]\n\n",
            (argc > 2) ? "file-num " : "");
        printf("    file-num is a number from the following list\n");

        for (int j = 1; j < argc; j++) {
            printf("        %2d  %-10s [%s locking]\n", j, argv[j],
                mandLockingEnabled(fdList[j]) ? "mandatory" : "advisory");
        }
    }

    printf("    'cmd' is 'g' (GETLK), 's' (SETLK), or 'w' (SETLKW)\n");
    printf("        or for OFD locks: 'G' (OFD_GETLK), 'S' (OFD_SETLK), or "
        "'W' (OFD_SETLKW)\n");
    printf("    'lock' is 'r' (READ), 'w' (WRITE), or 'u' (UNLOCK)\n");
    printf("    'start' and 'length' specify byte range to lock\n");
    printf("    'whence' is 's' (SEEK_SET, default), 'c' (SEEK_CUR), "
        "or 'e' (SEEK_END)\n\n");
}

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s file...\n", argv[0]);
    }

    int *fdList = calloc(argc, sizeof(int));
    if (fdList == NULL) {
        err_sys("calloc");
    }

    int j = 0;
    for (j = 1; j < argc; j++) {
        fdList[j] = open(argv[j], O_RDWR);
        if (fdList[j] == -1) {
            err_sys("open (%s)", argv[j]);
        }
    }

    printf("File       Locking\n");
    printf("----       -------\n");

    for (j = 1; j < argc; j++)
        printf("%-10s %s\n", argv[j], mandLockingEnabled(fdList[j]) ?
                "mandatory" : "advisory");
    printf("\n");
    printf("Enter ? for help\n");
    char line[MAX_LINE] = {0};
    int numRead = 0;
        struct flock fl;

    for (;;) {
        printf("PID=%ld> ", (long) getpid());
        fflush(stdout);

        if (fgets(line, MAX_LINE, stdin) == NULL) {
            exit(EXIT_SUCCESS);
        }

        line[strlen(line) - 1] = '\0';
        if (*line == '\0') {
            continue;
        }

        if (line[0] == '?') {
            displayCmdFmt(argc, argv, fdList);
            continue;
        }

        char whence = 's';
        char cmdCh = 0;
        char lock = 0;
        int fileNum = 0;
        long long len = 0;
        long long st = 0;

        if (argc == 2) {
            fileNum = 1;
            numRead = sscanf(line, " %c %c %lld %lld %c",
                &cmdCh, &lock, &st, &len, &whence);
        } else {
            numRead = sscanf(line, "%d %c %c %lld %lld %c",
                &fileNum, &cmdCh, &lock, &st, &len, &whence);
        }

        fl.l_start = st;
        fl.l_len = len;

        if (fileNum < 1 || fileNum >= argc) {
            printf("File number must be in range 1 to %d\n", argc-1);
            continue;
        }

        int fd = fdList[fileNum];
        if (!((numRead >= 4 && argc == 2) || (numRead >= 5 && argc > 2)) ||
            strchr("gswGSW", cmdCh) == NULL ||
            strchr("rwu", lock) == NULL || strchr("sce", whence) == NULL) {
            printf("Invalid command!\n");
            continue;
        }

        int cmd = (cmdCh == 'G') ? F_OFD_GETLK : (cmdCh == 'S') ? F_OFD_SETLK :
            (cmdCh == 'W') ? F_OFD_SETLKW :
            (cmdCh == 'g') ? F_GETLK : (cmdCh == 's') ? F_SETLK : F_SETLKW;
        fl.l_pid = 0;
        fl.l_type = (lock == 'r') ? F_RDLCK : (lock == 'w') ? F_WRLCK : F_UNLCK;
        fl.l_whence = (whence == 'c') ? SEEK_CUR :
            (whence == 'e') ? SEEK_END : SEEK_SET;

        int status = fcntl(fd, cmd, &fl);
        if (cmd == F_GETLK || cmd == F_OFD_GETLK) {
            if (status == -1) {
                err_msg("fcntl");
            } else {
                if (fl.l_type == F_UNLCK) {
                    printf("[PID=%ld] Lock can be placed\n", (long) getpid());
                } else {
                    printf("[PID=%ld] Denied by %s lock on %lld:%lld "
                        "(held by PID %ld)\n", (long) getpid(),
                        (fl.l_type == F_RDLCK) ? "READ" : "WRITE",
                        (long long) fl.l_start,
                        (long long) fl.l_len, (long) fl.l_pid);
                }
            }
        } else {
            if (status == 0) {
                printf("[PID=%ld] %s\n", (long) getpid(),
                    (lock == 'u') ? "unlocked" : "got lock");
            } else if (errno == EAGAIN || errno == EACCES) {
                printf("[PID=%ld] failed (incompatible lock)\n", (long) getpid());
            } else if (errno == EDEADLK) {
                printf("[PID=%ld] failed (deadlock)\n", (long) getpid());
            } else {
                err_msg("fcntl");
            }
        }
    }
}
