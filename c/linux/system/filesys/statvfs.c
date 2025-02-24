#include "common.h"
#include <sys/statvfs.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s path\n", argv[0]);
    }

    struct statvfs sb = {0};
    if (statvfs(argv[1], &sb) == -1) {
        err_quit("statvfs");
    }

    printf("Block size                       %lu\n", sb.f_bsize);
    printf("Fundamental block size           %lu\n", sb.f_frsize);
    printf("Total blocks (in above units)    %lu\n",
            (unsigned long) sb.f_blocks);
    printf("Free blocks for priv. proc.      %lu\n",
            (unsigned long) sb.f_bfree);
    printf("Free blocks for unpriv. proc.    %lu\n",
            (unsigned long) sb.f_bavail);
    printf("Total number of i-nodes          %lu\n",
            (unsigned long) sb.f_files);
    printf("Free i-nodes for priv. proc.     %lu\n",
            (unsigned long) sb.f_ffree);
    printf("Free i-nodes for nonpriv. proc.  %lu\n",
            (unsigned long) sb.f_favail);
    printf("File system ID                   %#lx\n", sb.f_fsid);
    printf("Flags                            %#lx\n", sb.f_flag);
    printf("Maximum filename length          %lu\n", sb.f_namemax);
    exit(EXIT_SUCCESS);
}

