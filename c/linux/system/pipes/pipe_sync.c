#include "curr_time.h"
#include "common.h"

// 执行示例: ./pipe_sync 4 2 6
int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s sleep-time...\n", argv[0]);
    }

    setbuf(stdout, NULL);
    printf("%s  Parent started\n", currTime("%T"));

    int pfd[2] = {0};
    if (pipe(pfd) == -1) {
        err_sys("pipe");
    }

    for (int j = 1; j < argc; j++) {
        switch (fork()) {
        case -1:
            err_sys("fork %d", j);
        case 0:
            if (close(pfd[0]) == -1) {
                err_sys("close");
            }

            sleep(atoi(argv[j]));
            printf("%s  Child %d (PID=%ld) closing pipe\n",
                currTime("%T"), j, (long) getpid());

            if (close(pfd[1]) == -1) {
                err_sys("close");
            }

            _exit(EXIT_SUCCESS);
        default:
            break;
        }
    }

    if (close(pfd[1]) == -1) {
        err_sys("close");
    }

    int dummy = 0;
    if (read(pfd[0], &dummy, 1) != 0) {
        err_quit("parent didn't get EOF");
    }

    printf("%s  Parent ready to go\n", currTime("%T"));
    exit(EXIT_SUCCESS);
}
