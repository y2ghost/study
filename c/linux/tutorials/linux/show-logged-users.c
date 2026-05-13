#include <stdio.h>
#include <utmp.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdlib.h>

#define UTMP_PATH "/var/run/utmp"

int main(void)
{
    int fd_utmp = 0;
    char user_name[UT_NAMESIZE+1] = {'\0'};
    struct utmp utmp_entry;

    /* open the utmp file for reading. */
    fd_utmp = open(UTMP_PATH, O_RDONLY);
    if (fd_utmp < 0) {
        perror("open");
        exit(1);
    }

    printf("Currently logged-in users:\n");
    while (1) {
        int rc = read(fd_utmp, &utmp_entry, sizeof(utmp_entry));

        if (rc < 0) {
            perror("read");
            exit(1);
        }

        if (0 == rc) {
            break;
        }

        if (USER_PROCESS != utmp_entry.ut_type) {
            continue;
        }

        snprintf(user_name, sizeof(user_name), "%s", utmp_entry.ut_name);
        printf("'%s',", user_name);
    }

    printf("\n");
    close(fd_utmp);
    return 0;
}
