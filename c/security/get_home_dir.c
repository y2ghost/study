#include <pwd.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>

int main(int argc, char *argv[]) {
    uid_t uid = 0;
    struct passwd *pwd = NULL;
    struct passwd save_pwd;

    uid = getuid();
    printf("User's UID is %d.\n", (int)uid);

    pwd = getpwuid(uid);
    if (NULL != pwd) {
        memcpy(&save_pwd, pwd, sizeof(save_pwd));
    }

    endpwent();
    if (NULL == pwd) {
        printf("Unable to get user's password file record!\n");
        return 1;
    }
    
    pwd = NULL;
    printf("User's home directory is %s\n", save_pwd.pw_dir);
    return 0;
}
