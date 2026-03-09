#include "common.h"

#define MAXARGC 50
#define WHITE   " \t\n"

/*
 * 就地分析修改buf字符串数组
 * 返回-1表示出错，其他值同optfunc函数返回值
 */
int buf_args(char *buf, int (*optfunc)(int, char **))
{
    int argc = 0;
    char *tok = NULL;
    char *str = NULL;
    char *argv[MAXARGC] = {NULL};

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
     * 只拷贝指针值，即使函数返回后argv消失
     */
    return((*optfunc)(argc, argv));
}

