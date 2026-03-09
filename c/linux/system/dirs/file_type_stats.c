#include "common.h"
#include <ftw.h>

static int numReg = 0;
static int numDir = 0;
static int numSymLk = 0;
static int numSocket = 0;
static int numFifo = 0;
static int numChar = 0;
static int numBlock = 0;
static int numNonstatable = 0;

static int countFile(const char *path, const struct stat *sb, int flag, struct FTW *ftwb)
{
    if (flag == FTW_NS) {
        numNonstatable++;
        return 0;
    }

    switch (sb->st_mode & S_IFMT) {
        case S_IFREG:  numReg++;    break;
        case S_IFDIR:  numDir++;    break;
        case S_IFCHR:  numChar++;   break;
        case S_IFBLK:  numBlock++;  break;
        case S_IFLNK:  numSymLk++;  break;
        case S_IFIFO:  numFifo++;   break;
        case S_IFSOCK: numSocket++; break;
    }

    return 0;
}

static void printStats(const char *msg, int num, int numFiles)
{
    printf("%-15s   %6d %6.1f%%\n", msg, num, num * 100.0 / numFiles);
}

int main(int argc, char *argv[])
{
    if (argc != 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s dir-path\n", argv[0]);
    }

    if (nftw(argv[1], &countFile, 20, FTW_PHYS) == -1) {
        err_quit("nftw");
    }

    int numFiles = numReg + numDir + numSymLk + numSocket +
        numFifo + numChar + numBlock + numNonstatable;

    if (numFiles == 0) {
        printf("No files found\n");
    } else {
        printf("Total files:      %6d\n", numFiles);
        printStats("Regular:", numReg, numFiles);
        printStats("Directory:", numDir, numFiles);
        printStats("Char device:", numChar, numFiles);
        printStats("Block device:", numBlock, numFiles);
        printStats("Symbolic link:", numSymLk, numFiles);
        printStats("FIFO:", numFifo, numFiles);
        printStats("Socket:", numSocket, numFiles);
        printStats("Non-statable:", numNonstatable, numFiles);
    }
    exit(EXIT_SUCCESS);
}

