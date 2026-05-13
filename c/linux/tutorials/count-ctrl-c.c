#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>

#define CTRL_C_THRESHOLD    5

static int ctrl_c_count = 0;

void catch_int(int sig_num)
{
    sigset_t old_set;
    sigset_t new_set;

    signal(SIGINT, catch_int);
    sigfillset(&new_set);
    sigprocmask(SIG_SETMASK, &new_set, &old_set);

    ctrl_c_count++;
    if (ctrl_c_count >= CTRL_C_THRESHOLD) {
        char answer[30] = {'\0'};

        printf("\nRealy Exit? [y/N]: ");
        fflush(stdout);

        fgets(answer, sizeof(answer), stdin);
        if ('y'==answer[0] || 'Y'==answer[0]) {
            printf("\nExiting...\n");
            fflush(stdout);
            exit(0);
        } else {
            printf("\nContinuing\n");
            fflush(stdout);
            ctrl_c_count = 0;
        }
    }

    sigprocmask(SIG_SETMASK, &old_set, NULL);
}

void catch_suspend(int sig_num)
{
    sigset_t old_set;
    sigset_t new_set;

    signal(SIGTSTP, catch_suspend);
    sigfillset(&new_set);
    sigprocmask(SIG_SETMASK, &new_set, &old_set);
    printf("\n\nSo far, '%d' Ctrl-C presses were counted\n\n", ctrl_c_count);
    fflush(stdout);
    sigprocmask(SIG_SETMASK, &old_set, NULL);
}

int main(int argc, char* argv[])
{
    signal(SIGINT, catch_int);
    signal(SIGTSTP, catch_suspend);

    while (1) {
        pause();
    }

    return 0;
}
