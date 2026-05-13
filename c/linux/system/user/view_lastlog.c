#include "ugid.h"
#include "common.h"
#include <time.h>
#include <lastlog.h>
#include <paths.h>
#include <fcntl.h>

int main(int argc, char *argv[])
{
    if (argc > 1 && strcmp(argv[1], "--help") == 0) {
        err_quit("%s [username...]\n", argv[0]);
    }

    int fd = open(_PATH_LASTLOG, O_RDONLY);
    if (fd == -1) {
        err_sys("open");
    }

    struct lastlog llog;
    for (int j = 1; j < argc; j++) {
        uid_t uid = get_uid_byname(argv[j]);
        if (uid == -1) {
            printf("No such user: %s\n", argv[j]);
            continue;
        }

        if (lseek(fd, uid * sizeof(struct lastlog), SEEK_SET) == -1) {
            err_sys("lseek");
        }

        if (read(fd, &llog, sizeof(struct lastlog)) <= 0) {
            printf("read failed for %s\n", argv[j]);
            continue;
        }

        time_t ll_time = llog.ll_time;
        printf("%-8.8s %-6.6s %-20.20s %s", argv[j], llog.ll_line,
            llog.ll_host, ctime((time_t *) &ll_time));
    }

    close(fd);
    exit(EXIT_SUCCESS);
}

