#include "common.h"
#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/fs.h>

static void usageError(const char *progName)
{
    fprintf(stderr, "Usage: %s {+-=}{attrib-chars} file...\n\n", progName);
#define fpe(str) fprintf(stderr, "    " str)            /* Shorter! */
    fpe("+ add attribute; - remove attribute; "
                        "= set attributes absolutely\n\n");
    fpe("'attrib-chars' contains one or more of:\n");
    fpe("    a   Force open() to include O_APPEND "
                        "(privilege required)\n");
    fpe("    A   Do not update last access time\n");
    fpe("    c   Compress (requires e2compr package)\n");
    fpe("    d   Do not backup with dump(8)\n");
    fpe("    D   Synchronous directory updates\n");
    fpe("    i   Immutable (privilege required)\n");
    fpe("    j   Enable ext3/ext4 data journaling\n");
    fpe("    s   Secure deletion (not implemented)\n");
    fpe("    S   Synchronous file updates\n");
    fpe("    t   Disable tail-packing (Reiserfs only)\n");
    fpe("    T   Mark as top-level directory for Orlov algorithm\n");
    fpe("    u   Undelete (not implemented)\n");
    exit(EXIT_FAILURE);
}

int main(int argc, char *argv[])
{
    if (argc < 3 || strchr("+-=", argv[1][0]) == NULL ||
            strcmp(argv[1], "--help") == 0) {
        usageError(argv[0]);
    }

    int attr = 0;
    for (char *p = &argv[1][1]; *p != '\0'; p++) {
        switch (*p) {
        case 'a': attr |= FS_APPEND_FL;         break;
        case 'A': attr |= FS_NOATIME_FL;        break;
        case 'c': attr |= FS_COMPR_FL;          break;
        case 'd': attr |= FS_NODUMP_FL;         break;
        case 'D': attr |= FS_DIRSYNC_FL;        break;
        case 'i': attr |= FS_IMMUTABLE_FL;      break;
        case 'j': attr |= FS_JOURNAL_DATA_FL;   break;
        case 's': attr |= FS_SECRM_FL;          break;
        case 'S': attr |= FS_SYNC_FL;           break;
        case 't': attr |= FS_NOTAIL_FL;         break;
        case 'T': attr |= FS_TOPDIR_FL;         break;
        case 'u': attr |= FS_UNRM_FL;           break;
        default:  usageError(argv[0]);
        }
    }

    for (int j = 2; j < argc; j++) {
        int fd = open(argv[j], O_RDONLY);
        if (fd == -1) {
            err_quit("open: %s", argv[j]);
            continue;
        }

        if (argv[1][0] == '+' || argv[1][0] == '-') {
            int oldAttr = 0;
            if (ioctl(fd, FS_IOC_GETFLAGS, &oldAttr) == -1) {
                err_quit("ioctl1: %s", argv[j]);
            }

            attr = (*argv[1] == '-') ? (oldAttr & ~attr) : (oldAttr | attr);
        }

        if (ioctl(fd, FS_IOC_SETFLAGS, &attr) == -1) {
            err_quit("ioctl2: %s", argv[j]);
        }

        if (close(fd) == -1) {
            err_quit("close");
        }
    }

    exit(EXIT_SUCCESS);
}

