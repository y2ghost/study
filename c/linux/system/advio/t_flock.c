#include "curr_time.h"
#include "common.h"
#include <sys/file.h>
#include <fcntl.h>
#include <errno.h>

/*
 * 操作示例
 * touch lock_file
 * ./t_flock lock_file s 60 &
 * ./t_flock lock_file s 2
 * ./t_flock lock_file xn
 */
int main(int argc, char *argv[])
{
    if (argc < 3 || strcmp(argv[1], "--help") == 0 ||
        strchr("sx", argv[2][0]) == NULL) {
        err_quit("%s file lock [sleep-time]\n"
            "    'lock' is 's' (shared) or 'x' (exclusive)\n"
            "        optionally followed by 'n' (nonblocking)\n"
            "    'sleep-time' specifies time to hold lock\n", argv[0]);
    }

    int lock = (argv[2][0] == 's') ? LOCK_SH : LOCK_EX;
    if (argv[2][1] == 'n') {
        lock |= LOCK_NB;
    }

    int fd = open(argv[1], O_RDONLY);
    if (fd == -1) {
        err_sys("open");
    }

    const char *lname = (lock & LOCK_SH) ? "LOCK_SH" : "LOCK_EX";
    printf("PID %ld: requesting %s at %s\n", (long) getpid(), lname,
        currTime("%T"));

    if (flock(fd, lock) == -1) {
        if (errno == EWOULDBLOCK) {
            err_quit("PID %ld: already locked - bye!", (long) getpid());
        } else {
            err_sys("flock (PID=%ld)", (long) getpid());
        }
    }

    printf("PID %ld: granted    %s at %s\n", (long) getpid(), lname,
        currTime("%T"));
    sleep((argc > 3) ? atoi(argv[3]) : 10);
    printf("PID %ld: releasing  %s at %s\n", (long) getpid(), lname,
        currTime("%T"));

    if (flock(fd, LOCK_UN) == -1) {
        err_sys("flock");
    }

    exit(EXIT_SUCCESS);
}

