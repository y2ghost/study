#include <ugid.h>
#include <pwd.h>
#include <grp.h>
#include <stdlib.h>

char *get_name_byuid(uid_t uid)
{
    char *name = NULL;
    struct passwd *pwd = getpwuid(uid);

    if (NULL != pwd) {
        name = pwd->pw_name;
    }

    return name;
}

uid_t get_uid_byname(const char *name)
{
    if (NULL==name || '\0' == *name) {
        return -1;
    }

    char *endptr = NULL;
    uid_t uid = strtol(name, &endptr, 10);

    if ('\0' == *endptr) {
        return uid;
    }

    struct passwd *pwd = getpwnam(name);
    if (NULL != pwd) {
        uid = pwd->pw_uid;
    }

    return uid;
}

char *get_gname_bygid(gid_t gid)
{
    char *gname = NULL;
    struct group *grp = getgrgid(gid);

    if (NULL != grp) {
        gname = grp->gr_name;
    }

    return gname;
}

gid_t get_gid_bygname(const char *gname)
{
    if (NULL==gname || '\0'==*gname) {
        return -1;
    }

    char *endptr = NULL;
    gid_t gid = strtol(gname, &endptr, 10);

    if ('\0' == *gname) {
        return gid;
    }

    struct group *grp = getgrnam(gname);
    if (NULL != grp) {
        gid = grp->gr_gid;
    }

    return gid;
}
