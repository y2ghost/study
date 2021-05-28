#include "apue.h"

#define MAXARGC 50
#define WHITE   " \t\n"

/*
 * buf[] contains white-space-separated arguments.  We convert it to an
 * argv-style array of pointers, and call the user's function (optfunc)
 * to process the array.  We return -1 if there's a problem parsing buf,
 * else we return whatever optfunc() returns.  Note that user's buf[]
 * array is modified (nulls placed after each token).
 */
int buf_args(char *buf, int (*optfunc)(int, char **))
{
    int argc = 0;
    char *tok = NULL;
    char *str = NULL;
    char *argv[MAXARGC] = {NULL};

    argc = 0;
    for (str=buf; argc<MAXARGC-1; str=NULL) {
        tok = strtok(str, WHITE);
        if (NULL == tok) {
            break;
        }

        argv[argc] = tok;
        argc++;
    }

    argv[argc] = NULL;
    if (0 == argc) {
        return -1;
    }

    /*
     * Since argv[] pointers point into the user's buf[],
     * user's function can just copy the pointers, even
     * though argv[] array will disappear on return.
     */
    return((*optfunc)(argc, argv));
}
