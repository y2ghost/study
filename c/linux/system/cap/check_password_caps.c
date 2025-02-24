#include "common.h"
#include <sys/capability.h>
#include <unistd.h>
#include <limits.h>
#include <pwd.h>
#include <shadow.h>
#include <errno.h>

static int modifyCap(int capability, int setting)
{
    cap_t caps = cap_get_proc();
    if (caps == NULL) {
        return -1;
    }

    cap_value_t capList[1] = {capability};
    if (cap_set_flag(caps, CAP_EFFECTIVE, 1, capList, setting) == -1) {
        cap_free(caps);
        return -1;
    }

    if (cap_set_proc(caps) == -1) {
        cap_free(caps);
        return -1;
    }

    if (cap_free(caps) == -1) {
        return -1;
    }

    return 0;
}

static int raiseCap(int capability)
{
    return modifyCap(capability, CAP_SET);
}

static int dropAllCaps(void)
{
    cap_t empty = cap_init();
    if (empty == NULL) {
        return -1;
    }

    int s = cap_set_proc(empty);
    if (cap_free(empty) == -1) {
        return -1;
    }

    return s;
}

/*
 * 执行示例
 * sudo setcap "cap_dac_read_search=p" ./check_password_caps
 * getcap check_password_caps
 * ./check_password_caps
 */
int main(int argc, char *argv[])
{
    long lnmax = sysconf(_SC_LOGIN_NAME_MAX);
    if (lnmax == -1) {
        lnmax = 256;
    }

    char *username = malloc(lnmax);
    if (username == NULL) {
        err_sys("malloc");
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

    if (raiseCap(CAP_DAC_READ_SEARCH) == -1) {
        err_quit("raiseCap() failed");
    }

    struct spwd *spwd = getspnam(username);
    if (spwd == NULL && errno == EACCES) {
        err_quit("no permission to read shadow password file");
    }

    if (dropAllCaps() == -1) {
        err_quit("dropAllCaps() failed");
    }

    if (spwd != NULL) {
        pwd->pw_passwd = spwd->sp_pwdp;
    }

    char *password = getpass("Password: ");
    char *encrypted = crypt(password, pwd->pw_passwd);

    for (char *p = password; *p != '\0'; ) {
        *p++ = '\0';
    }

    if (encrypted == NULL) {
        err_sys("crypt");
    }

    int authOk = strcmp(encrypted, pwd->pw_passwd) == 0;
    if (!authOk) {
        printf("Incorrect password\n");
        exit(EXIT_FAILURE);
    }

    printf("Successfully authenticated: UID=%ld\n", (long) pwd->pw_uid);
    exit(EXIT_SUCCESS);
}
