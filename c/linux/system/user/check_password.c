#include "common.h"
#include <unistd.h>
#include <limits.h>
#include <pwd.h>
#include <shadow.h>
#include <errno.h>

int main(int argc, char *argv[])
{
    long lnmax = sysconf(_SC_LOGIN_NAME_MAX);
    if (lnmax == -1) {
        lnmax = 256;
    }


    char *username = malloc(lnmax);
    if (username == NULL) {
        err_quit("malloc");
    }

    printf("Username: ");
    fflush(stdout);

    if (fgets(username, lnmax, stdin) == NULL) {
        exit(EXIT_FAILURE);
    }

    size_t len = strlen(username);
    if (username[len - 1] == '\n') {
        username[len - 1] = '\0';
    }

    struct passwd *pwd = getpwnam(username);
    if (pwd == NULL) {
        err_quit("couldn't get password record");
    }

    struct spwd *spwd = getspnam(username);
    if (spwd == NULL && errno == EACCES) {
        err_quit("no permission to read shadow password file");
    }

    if (spwd != NULL) {
        pwd->pw_passwd = spwd->sp_pwdp;
    }

    char *password = getpass("Password: ");
    char *encrypted = crypt(password, pwd->pw_passwd);

    // 立即删除缓存的密码
    for (char *p = password; *p != '\0'; ) {
        *p++ = '\0';
    }

    if (encrypted == NULL) {
        err_quit("crypt");
    }

    if (0 != strcmp(encrypted, pwd->pw_passwd)) {
        printf("Incorrect password\n");
        exit(EXIT_FAILURE);
    }

    printf("Successfully authenticated: UID=%ld\n", (long) pwd->pw_uid);
    exit(EXIT_SUCCESS);
}

