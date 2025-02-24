#include "common.h"
#include "ugid.h"
#include <unistd.h>
#include <sys/fsuid.h>
#include <limits.h>

#define SG_SIZE (NGROUPS_MAX + 1)

// 显示进程的用户ID和组ID等
int main(int argc, char *argv[])
{
    uid_t ruid, euid, suid, fsuid;
    if (getresuid(&ruid, &euid, &suid) == -1) {
        err_quit("getresuid");
    }

    gid_t rgid, egid, sgid, fsgid;
    if (getresgid(&rgid, &egid, &sgid) == -1) {
        err_quit("getresgid");
    }

    // 尝试改变文件系统ID
    fsuid = setfsuid(0);
    fsgid = setfsgid(0);

    printf("UID: ");
    char *p = get_name_byuid(ruid);
    printf("real=%s (%ld); ", (p == NULL) ? "???" : p, (long) ruid);
    p = get_name_byuid(euid);
    printf("eff=%s (%ld); ", (p == NULL) ? "???" : p, (long) euid);
    p = get_name_byuid(suid);
    printf("saved=%s (%ld); ", (p == NULL) ? "???" : p, (long) suid);
    p = get_name_byuid(fsuid);
    printf("fs=%s (%ld); ", (p == NULL) ? "???" : p, (long) fsuid);
    printf("\n");

    printf("GID: ");
    p = get_gname_bygid(rgid);
    printf("real=%s (%ld); ", (p == NULL) ? "???" : p, (long) rgid);
    p = get_gname_bygid(egid);
    printf("eff=%s (%ld); ", (p == NULL) ? "???" : p, (long) egid);
    p = get_gname_bygid(sgid);
    printf("saved=%s (%ld); ", (p == NULL) ? "???" : p, (long) sgid);
    p = get_gname_bygid(fsgid);
    printf("fs=%s (%ld); ", (p == NULL) ? "???" : p, (long) fsgid);
    printf("\n");

    gid_t suppGroups[SG_SIZE];
    int numGroups = getgroups(SG_SIZE, suppGroups);
    if (numGroups == -1)
        err_quit("getgroups");

    printf("Supplementary groups (%d): ", numGroups);
    for (int j = 0; j < numGroups; j++) {
        p = get_gname_bygid(suppGroups[j]);
        printf("%s (%ld) ", (p == NULL) ? "???" : p, (long) suppGroups[j]);
    }

    printf("\n");
    exit(EXIT_SUCCESS);
}

