#include "common.h"
#include "ugid.h"
#include <acl/libacl.h>
#include <sys/acl.h>

static void usageError(char *progName)
{
    fprintf(stderr, "Usage: %s [-d] filename\n", progName);
    exit(EXIT_FAILURE);
}

int main(int argc, char *argv[])
{
    int opt = 0;
    acl_type_t type = ACL_TYPE_ACCESS;

    while ((opt = getopt(argc, argv, "d")) != -1) {
        switch (opt) {
        case 'd': type = ACL_TYPE_DEFAULT;      break;
        case '?': usageError(argv[0]);
        }
    }

    if (optind + 1 != argc)
        usageError(argv[0]);

    acl_t acl = acl_get_file(argv[optind], type);
    if (acl == NULL) {
        err_quit("acl_get_file");
    }

    for (int entryId = ACL_FIRST_ENTRY; ; entryId = ACL_NEXT_ENTRY) {
        acl_entry_t entry = {0};
        if (acl_get_entry(acl, entryId, &entry) != 1) {
            break;
        }

        acl_tag_t tag = {0};
        if (acl_get_tag_type(entry, &tag) == -1) {
            err_quit("acl_get_tag_type");
        }

        printf("%-12s", (tag == ACL_USER_OBJ) ?  "user_obj" :
                        (tag == ACL_USER) ?      "user" :
                        (tag == ACL_GROUP_OBJ) ? "group_obj" :
                        (tag == ACL_GROUP) ?     "group" :
                        (tag == ACL_MASK) ?      "mask" :
                        (tag == ACL_OTHER) ?     "other" : "???");
        char *name = NULL;

        if (tag == ACL_USER) {
            uid_t *uidp = acl_get_qualifier(entry);
            if (uidp == NULL) {
                err_quit("acl_get_qualifier");
            }

            name = get_name_byuid(*uidp);
            if (name == NULL) {
                printf("%-8d ", *uidp);
            } else {
                printf("%-8s ", name);
            }

            if (acl_free(uidp) == -1) {
                err_quit("acl_free");
            }
        } else if (tag == ACL_GROUP) {
            gid_t *gidp = acl_get_qualifier(entry);
            if (gidp == NULL) {
                err_quit("acl_get_qualifier");
            }

            name = get_gname_bygid(*gidp);
            if (name == NULL) {
                printf("%-8d ", *gidp);
            } else {
                printf("%-8s ", name);
            }

            if (acl_free(gidp) == -1) {
                err_quit("acl_free");
            }
        } else {
            printf("         ");
        }

        acl_permset_t permset = {0};
        if (acl_get_permset(entry, &permset) == -1) {
            err_quit("acl_get_permset");
        }

        int permVal = acl_get_perm(permset, ACL_READ);
        if (permVal == -1) {
            err_quit("acl_get_perm - ACL_READ");
        }

        printf("%c", (permVal == 1) ? 'r' : '-');
        permVal = acl_get_perm(permset, ACL_WRITE);

        if (permVal == -1) {
            err_quit("acl_get_perm - ACL_WRITE");
        }

        printf("%c", (permVal == 1) ? 'w' : '-');
        permVal = acl_get_perm(permset, ACL_EXECUTE);

        if (permVal == -1) {
            err_quit("acl_get_perm - ACL_EXECUTE");
        }

        printf("%c", (permVal == 1) ? 'x' : '-');
        printf("\n");
    }

    if (acl_free(acl) == -1) {
        err_quit("acl_free");
    }

    exit(EXIT_SUCCESS);
}
