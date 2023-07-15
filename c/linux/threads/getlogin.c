#include "errors.h"
#include <limits.h>

#ifndef TTY_NAME_MAX
#define TTY_NAME_MAX    128
#endif 
#ifndef LOGIN_NAME_MAX
#define LOGIN_NAME_MAX  32
#endif 
    
int main(int argc, char *argv[])
{
    int status = 0;
    char *cterm_str_ptr = NULL;
    char cterm_str[L_ctermid] = {'\0'};
    char stdin_str[TTY_NAME_MAX]= {'\0'};
    char login_str[LOGIN_NAME_MAX] = {'\0'};

    status = getlogin_r(login_str, sizeof(login_str));
    if (0 != status) {
        err_abort(status, "Get login");
    }

    cterm_str_ptr = ctermid(cterm_str);
    if (NULL == cterm_str_ptr) {
        errno_abort("Get cterm");
    }

    status = ttyname_r(0, stdin_str, sizeof(stdin_str));
    if (0 != status) {
        err_abort(status, "Get stdin");
    }

    printf("User: %s, cterm: %s, fd 0: %s\n", login_str, cterm_str, stdin_str);
    return 0;
}
