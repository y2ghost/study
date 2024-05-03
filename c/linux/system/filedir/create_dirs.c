#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>

int main(int ac, char *av[])
{
    if (2 != ac) {
        printf("%s: give a file path to create it's parent dirs!\n", av[0]);
        exit(1);
    }

    char *dname = strdup(av[1]);
    char *slash = dname;

    while (1) {
        slash = strchr(slash+1, '/');
        if (NULL == slash) {
            break;
        }

        *slash = '\0';
        mkdir(dname, 0755);
        *slash = '/';
    }

    free(dname);
    return 0;
}
