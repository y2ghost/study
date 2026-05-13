#include "cap_functions.h"
#include "common.h"
#include <sys/prctl.h>
#include <sys/capability.h>
#include <linux/securebits.h>
#include <pwd.h>
#include <grp.h>
#include <errno.h>

static void usage(char *pname)
{
    fprintf(stderr, "Usage: %s [-A] user cap,... cmd arg...\n", pname);
    fprintf(stderr, "\t'user' is the user with whose credentials the\n");
    fprintf(stderr, "\t\tprogram is to be launched\n");
    fprintf(stderr, "\t'cap,...' is the set of capabilities with which the\n");
    fprintf(stderr, "\t\tprogram is to be launched\n");
    fprintf(stderr, "\t'cmd' and 'arg...' specify the command plus arguments\n");
    fprintf(stderr, "\t\tfor the program that is to be launched\n");
    fprintf(stderr, "\n");
    fprintf(stderr, "\tOptions:\n");
    fprintf(stderr, "\t    -A  Raise the specified capabilities only in the "
        "inheritable set\n");
    fprintf(stderr, "\t\t(and not in ambient set) before launching 'cmd'\n");
    exit(EXIT_FAILURE);
}

static void setSupplementaryGroupList(char *user, gid_t gid)
{
    int ngroups = 0;
    getgrouplist(user, gid, NULL, &ngroups);
    gid_t *groups = calloc(ngroups, sizeof(gid_t));

    if (groups == NULL) {
        err_sys("calloc");
    }

    if (getgrouplist(user, gid, groups, &ngroups) == -1) {
        err_sys("getgrouplist");
    }

    if (setgroups(ngroups, groups) == -1) {
        err_sys("setgroups");
    }
}

static void setCredentials(char *user)
{
    struct passwd *pwd = getpwnam(user);
    if (pwd == NULL) {
        fprintf(stderr, "Unknown user: %s\n", user);
        exit(EXIT_FAILURE);
    }

    setSupplementaryGroupList(user, pwd->pw_gid);
    if (setresgid(pwd->pw_gid, pwd->pw_gid, pwd->pw_gid) == -1) {
        err_sys("setresgid");
    }

    if (setresuid(pwd->pw_uid, pwd->pw_uid, pwd->pw_uid) == -1) {
        err_sys("setresuid");
    }
}

static cap_value_t capFromName(char *p)
{
    cap_value_t cap;
    if (cap_from_name(p, &cap) == -1) {
        fprintf(stderr, "Unrecognized capability name: %s\n", p);
        exit(EXIT_FAILURE);
    }

    return cap;
}

static void raiseCap(cap_value_t cap, char *capName, bool raiseAmbient)
{
    if (modifyCapSetting(CAP_INHERITABLE, cap, CAP_SET) == -1) {
        fprintf(stderr, "Could not raise '%s' inheritable "
            "capability (%s)\n", capName, strerror(errno));
        exit(EXIT_FAILURE);
    }

    if (raiseAmbient) {
        if (prctl(PR_CAP_AMBIENT, PR_CAP_AMBIENT_RAISE, cap, 0, 0) == -1) {
            fprintf(stderr, "Could not raise '%s' ambient "
                "capability (%s)\n", capName, strerror(errno));
            exit(EXIT_FAILURE);
        }
    }
}

static void raiseInheritableAndAmbientCaps(char *capList, bool raiseAmbient)
{
    for (char *capName = capList; (capName = strtok(capName, ",")); capName = NULL) {
        cap_value_t cap = capFromName(capName);
        raiseCap(cap, capName, raiseAmbient);
    }
}

int main(int argc, char *argv[])
{
    bool raiseAmbient = true;
    int opt = 0;

    while ((opt = getopt(argc, argv, "A")) != -1) {
        switch (opt) {
        case 'A':
            raiseAmbient = false;
            break;
        default:
            fprintf(stderr, "Bad option\n");
            usage(argv[0]);
            break;
        }
    }

    if (argc < optind + 3) {
        usage(argv[0]);
    }

    if (geteuid() != 0) {
        err_quit("Must be run as root");
    }

    if (prctl(PR_SET_SECUREBITS, SECBIT_NO_SETUID_FIXUP, 0, 0, 0) == -1) {
        err_sys("prctl");
    }

    setCredentials(argv[optind]);
    raiseInheritableAndAmbientCaps(argv[optind + 1], raiseAmbient);
    execvp(argv[optind + 2], &argv[optind + 2]);
    err_sys("execvp");
}
