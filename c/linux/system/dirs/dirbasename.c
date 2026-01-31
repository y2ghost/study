#include "common.h"
#include <libgen.h>

int main(int argc, char *argv[])
{
    for (int j = 1; j < argc; j++)  {
        char *t1 = strdup(argv[j]);
        if (t1 == NULL) {
            err_quit("strdup");
        }

        char *t2 = strdup(argv[j]);
        if (t2 == NULL) {
            err_quit("strdup");
        }

        printf("%s ==> %s + %s\n", argv[j], dirname(t1), basename(t2));
        free(t1);
        free(t2);
    }

    exit(EXIT_SUCCESS);
}

