#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <paths.h>
#include <limits.h>

extern char **environ;

/* These arrays are both NULL-terminated. */
static char *spc_restricted_environ[] = {
    "IFS= \t\n",
    "PATH=" _PATH_STDPATH,
    NULL,
};

static char *spc_preserve_environ[] = {
    "TZ",
    NULL,
};

/* preservev array NULL-terminated. */
void _init_environ(char *preservev[])
{
    size_t new_size = 0;
    size_t array_count = 1;  /* default NULL element */

    char **env = spc_restricted_environ;
    char *var = NULL;

    while (NULL != *env) {
        var = *env;
        env++;
        new_size += strlen(var) + 1;
        array_count++;
    }

    env = spc_preserve_environ;
    while (NULL != *env) {
        var = *env;
        env++;

        char *value = getenv(var);
        if (NULL == value) {
            continue;
        }

        new_size += strlen(var) + strlen(value) + 2;
        array_count++;
    }

    env = preservev;
    while (NULL!=env && NULL!=*env) {
        var = *env;
        env++;

        char *value = getenv(var);
        if (NULL == value) {
            continue;
        }

        new_size += strlen(var) + strlen(value) + 2;
        array_count++;
    }

    size_t array_size = array_count * sizeof(char*);
    new_size +=  array_size;
    char **new_environ = (char**)malloc(new_size);

    if (NULL == new_environ) {
        abort();
    }

    new_environ[array_count-1] = NULL;
    char *ptr = (char*)new_environ + array_size;

    size_t array_index = 0;
    env = spc_restricted_environ;

    while (NULL != *env) {
        var = *env;
        env++;
        new_environ[array_index++] = ptr;
        size_t len = strlen(var) + 1;
        memcpy(ptr, var, len);
        ptr += len;
    }

    env = spc_preserve_environ;
    while (NULL != *env) {
        var = *env;
        env++;

        char *value = getenv(var);
        if (NULL == value) {
            continue;
        }

        new_environ[array_index++] = ptr;
        size_t len = strlen(var);
        memcpy(ptr, var, len);
        *(ptr+len+1) = '=';
        memcpy(ptr+len+2, value, strlen(value)+1);
        ptr += len + strlen(value) + 2;
    }

    env = preservev;
    while (NULL!=env && NULL!=*env) {
        var = *env;
        env++;

        char *value = getenv(var);
        if (NULL == value) {
            continue;
        }

        new_environ[array_index++] = ptr;
        size_t len = strlen(var);
        memcpy(ptr, var, len);
        *(ptr+len) = '=';
        memcpy(ptr+len+1, value, strlen(value)+1);
        ptr += len + strlen(value) + 2;
    }

    environ = new_environ;
}

int main(int ac, char *av[])
{
    char *var_keep[] = {
        "USER", 
        "SHELL",
        NULL,
    };

    _init_environ(var_keep);
    char **env = environ;
    
    while (NULL != *env) {
        printf("%s\n", *env);
        env++;
    }
    
    return 0;
}
