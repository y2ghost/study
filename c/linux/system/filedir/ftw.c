#include <stdio.h>
#include <sys/stat.h>
#include <string.h>
#include <dirent.h>
#include <limits.h>

/* ftw types */
#define FTW_F   1   /* file other than directory */
#define FTW_D   2   /* directory */
#define FTW_DNR 3   /* directory that can't be read */
#define FTW_NS  4   /* file that we can't stat */

typedef int stat_func_t(const char *path,
    const struct stat *statptr, int ftw_type);

static stat_func_t _stat_func;
static int _myftw(char *path, stat_func_t *stat_func);
static int _dopath(const char *path, stat_func_t *stat_func);

enum ftype_e {
    UNKNOWN,
    REGULAR,
    DIRECTORY,
    BLOCK,
    CHAR,
    FIFO,
    SYMLINK,
    SOCKET,
};

struct ftype_t {
    const char *desc;
    long count;
} ftypes[] = {
    [UNKNOWN] = {"unknown files", 0},
    [REGULAR] = {"regular files", 0},
    [DIRECTORY] = {"directories", 0},
    [BLOCK] = {"block special", 0},
    [CHAR] = {"char special", 0},
    [FIFO] = {"FIFO special", 0},
    [SYMLINK] = {"symbolic links", 0},
    [SOCKET] = {"sockets special", 0},
};

int main(int ac, char *av[])
{
    if (2 != ac) {
        printf("usage: %s <starting-pathname>\n", av[0]);
        return 1;
    }

    long total = 0;
    int i = 0;

    int ret = _myftw(av[1], _stat_func);
    for (i=0; i<=SOCKET; ++i) {
        total += ftypes[i].count;
    }

    if (0 == total) {
        total = 1;
    }

    struct ftype_t *ftype = NULL;
    for (i=0; i<=SOCKET; ++i) {
        ftype = ftypes + i;
        printf("%s\t= %7ld, %5.2f %%\n", ftype->desc,
            ftype->count, ftype->count * 100.0 / total);
    }

    return ret;
}

static int _stat_func(const char *path, const struct stat *statptr,
    int ftw_type)
{
    enum ftype_e type = UNKNOWN;
    switch (ftw_type) {
    case FTW_F:
        switch (statptr->st_mode & S_IFMT) {
        case S_IFREG:
            type = REGULAR;
            break;
        case S_IFBLK:
            type = BLOCK;
            break;
        case S_IFCHR:
            type = CHAR;
            break;
        case S_IFIFO:
            type = FIFO;
            break;
        case S_IFSOCK:
            type = SOCKET;
            break;
        case S_IFDIR:
            printf("for S_FIDIR for %s\n", path);
            break;
        }
        break;
    case FTW_D:
        type = DIRECTORY;
        break;
    case FTW_DNR:
        printf("can't read directory %s\n", path);
        break;
    case FTW_NS:
        printf("stat error for %s\n", path);
        break;
    default:
        printf("unknown ftw type %d for pathname %s\n", ftw_type, path);
    }

    ftypes[type].count++;
    return 0;
}

static int _dopath(const char *path, stat_func_t *stat_func)
{
    struct stat statbuf = {0};
    if (lstat(path, &statbuf) < 0) {
        return stat_func(path, &statbuf, FTW_NS);
    }

    if (0 == S_ISDIR(statbuf.st_mode)) {
        return stat_func(path, &statbuf, FTW_F);
    }

    int do_ok = stat_func(path, &statbuf, FTW_D);
    if (0 != do_ok) {
        return do_ok;
    }

    DIR *dp = opendir(path);
    if (NULL == dp) {
        return stat_func(path, &statbuf, FTW_DNR);
    }

    struct dirent *dirp = NULL;
    char *dname = NULL;

    while (1) {
        dirp = readdir(dp);
        if (NULL == dirp) {
            break;
        }

        dname = dirp->d_name;
        if (0==strcmp(".", dname) || 0==strcmp("..",dname)) {
            continue;
        }

        char pathbuf[1024] = {'\0'};
        snprintf(pathbuf, sizeof(pathbuf), "%s/%s", path, dname);
        do_ok = _dopath(pathbuf, stat_func);
        if (0 != do_ok) {
            break;
        }
    }

    closedir(dp);
    return do_ok;
}

static int _myftw(char *path, stat_func_t *stat_func)
{
    char pathbuf[512] = {'\0'};

    snprintf(pathbuf, sizeof(pathbuf), "%s", path);
    return _dopath(pathbuf, _stat_func);
}
