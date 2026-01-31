#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>

static int my_unmy_setenv(const char *name)
{
    extern char **environ;
    if (name == NULL || name[0] == '\0' || strchr(name, '=') != NULL) {
        errno = EINVAL;
        return -1;
    }

    size_t len = strlen(name);
    for (char **ep = environ; *ep != NULL; ) {
        if (strncmp(*ep, name, len) == 0 && (*ep)[len] == '=') {
            for (char **sp = ep; *sp != NULL; sp++) {
                *sp = *(sp + 1);
            }
        } else {
            ep++;
        }
    }

    return 0;
}

static int my_setenv(const char *name, const char *value, int overwrite)
{
    if (name == NULL || name[0] == '\0' || strchr(name, '=') != NULL ||
            value == NULL) {
        errno = EINVAL;
        return -1;
    }

    if (getenv(name) != NULL && overwrite == 0) {
        return 0;
    }

    my_unmy_setenv(name);
    // +2给'=' 'NUL' 字符留空间
    char *es = malloc(strlen(name) + strlen(value) + 2);

    if (es == NULL) {
        return -1;
    }

    strcpy(es, name);
    strcat(es, "=");
    strcat(es, value);
    return (putenv(es) != 0) ? -1 : 0;
}

int main()
{
    if (putenv("TT=xxxxx") != 0) {
        perror("putenv");
    }

    system("echo '已设置TT变量'; printenv | grep ^TT");
    system("echo '变量个数:' `printenv | wc -l`");

    my_unmy_setenv("TT");
    system("echo '未设置TT变量'; printenv | grep ^TT");
    system("echo '变量个数:' `printenv | wc -l`");

    my_setenv("xyz", "one", 1);
    my_setenv("xyz", "two", 0);
    my_setenv("xyz2", "222", 0);
    system("echo '已设置x*变量'; printenv | grep ^x");
    system("echo '变量个数:' `printenv | wc -l`");
    exit(EXIT_SUCCESS);
}

