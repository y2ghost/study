#include "common.h"
#include <sys/statfs.h>

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s path\n", argv[0]);
    }

    struct statfs sfs;
    if (statfs(argv[1], &sfs) == -1) {
        err_quit("statfs");
    }

    printf("File system type:              %#lx\n",
            (unsigned long) sfs.f_type);
    printf("Optimal I/O block size:        %lu\n",
            (unsigned long) sfs.f_bsize);
    printf("Total data blocks:             %lu\n",
            (unsigned long) sfs.f_blocks);
    printf("Free data blocks:              %lu\n",
            (unsigned long) sfs.f_bfree);
    printf("Free blocks for nonsuperuser:  %lu\n",
            (unsigned long) sfs.f_bavail);
    printf("Total i-nodes:                 %lu\n",
            (unsigned long) sfs.f_files);
    printf("File system ID:                %#x, %#x\n",
            (unsigned) sfs.f_fsid.__val[0], (unsigned) sfs.f_fsid.__val[1]);
    printf("Free i-nodes:                  %lu\n",
            (unsigned long) sfs.f_ffree);
    printf("Maximum file name length:      %lu\n",
            (unsigned long) sfs.f_namelen);

    exit(EXIT_SUCCESS);
}

