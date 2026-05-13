#include "common.h"
#include <sys/xattr.h>
#include <sys/capability.h>
#include <linux/capability.h>
#include <errno.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s <file>\n", argv[0]);
    }

    struct vfs_ns_cap_data cap_data;
    ssize_t valueLen = getxattr(argv[1], "security.capability",
        (char *) &cap_data, sizeof(cap_data));

    if (valueLen == -1) { 
        if (errno == ENODATA) {
            err_quit("\"%s\" has no \"security.capability\" attribute", argv[1]);
        } else {
            err_sys("getxattr");
        }
    }

    printf("Capability version: %d",
        cap_data.magic_etc >> VFS_CAP_REVISION_SHIFT);
    if ((cap_data.magic_etc & VFS_CAP_REVISION_MASK) == VFS_CAP_REVISION_3) {
        printf("   [root ID = %u]", cap_data.rootid);
    }

    printf("\n");
    printf("Length of returned value = %zd\n", valueLen);
    printf("    Effective bit:   %d\n",
        cap_data.magic_etc & VFS_CAP_FLAGS_EFFECTIVE);
    printf("    Permitted set:   %08x %08x\n",
        cap_data.data[1].permitted, cap_data.data[0].permitted);
    printf("    Inheritable set: %08x %08x\n",
        cap_data.data[1].inheritable, cap_data.data[0].inheritable);
    exit(EXIT_SUCCESS);
}

