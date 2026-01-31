#include <stdio.h>
#include <unistd.h>
#include <signal.h>

void catch_int(int sig_num)
{
    signal(SIGINT, catch_int);
    printf("Don't do that\n");
    fflush(stdout);
}

int main(int ac, char **av)
{
    signal(SIGINT, catch_int);

    while (1) {
        pause();
    }

    return 0;
}
