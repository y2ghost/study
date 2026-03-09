#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>

void catch_alarm(int sig_num)
{
    printf("Operation timed out. Exiting...\n\n");
    exit(0);
}

int main(int argc, char* argv[])
{
    char user_name[40] = {'\0'};
    char *cr_pos = NULL;

    signal(SIGALRM, catch_alarm);
    printf("Username: ");
    fflush(stdout);
    alarm(30);
    fgets(user_name, sizeof(user_name), stdin);
    alarm(0);

    cr_pos = strchr(user_name, '\n');
    if (NULL != cr_pos) {
        *cr_pos = '\0';
    }

    printf("\nYour name: '%s'\n", user_name);
    return 0;
}
