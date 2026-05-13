#include "common.h"
#include <pwd.h>

int main(int argc, char *argv[])
{
    struct passwd *pwd = NULL;
    while ((pwd = getpwent()) != NULL) {
        printf("%-8s %5ld\n", pwd->pw_name, (long) pwd->pw_uid);
    }

    endpwent();
    exit(EXIT_SUCCESS);
}

