#include "apue.h"
#include <pwd.h>

static void my_alarm(int signo)
{
    struct passwd *rootptr = NULL;

    printf("in signal handler\n");
    rootptr = getpwnam("root");

    if (NULL == rootptr) {
            err_sys("getpwnam(root) error");
    }

    alarm(1);
}

int main(void)
{
    struct passwd *ptr = NULL;

    signal(SIGALRM, my_alarm);
    alarm(1);

    while (1) {
        ptr = getpwnam("sar");
        if (NULL == ptr) {
            err_sys("getpwnam error");
        }

        if (0 != strcmp(ptr->pw_name,"sar")) {
            printf("return value corrupted!, pw_name = %s\n",
                ptr->pw_name);
        }
    }
}
