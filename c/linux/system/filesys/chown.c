#include "ugid.h"
#include "common.h"
#include <pwd.h>
#include <grp.h>

int main(int argc, char *argv[])
{
    if (argc < 3 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s owner group [file...]\n"
                "        owner or group can be '-', "
                "meaning leave unchanged\n", argv[0]);
    }

    uid_t uid = 0;
    if (strcmp(argv[1], "-") == 0) {
        uid = -1;
    } else {
        uid = get_uid_byname(argv[1]);
        if (uid == -1) {
            err_quit("No such user (%s)", argv[1]);
        }
    }

    gid_t gid = 0;
    if (strcmp(argv[2], "-") == 0) {
        gid = -1;
    } else {
        gid = get_gid_bygname(argv[2]);
        if (gid == -1) {
            err_quit("No group user (%s)", argv[2]);
        }
    }

    int errFnd = 0;
    for (int j = 3; j < argc; j++) {
        if (chown(argv[j], uid, gid) == -1) {
            err_msg("chown: %s", argv[j]);
            errFnd = 1;
        }
    }

    exit(errFnd ? EXIT_FAILURE : EXIT_SUCCESS);
}

