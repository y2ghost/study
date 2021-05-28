#include    <sys/stat.h>
#include    <dirent.h>
#include    <limits.h>
#include    <string.h>
#include    <termios.h>
#include    <unistd.h>
#include    <stdlib.h>

struct devdir {
    struct devdir *d_next;
    char *d_name;
};

static struct devdir *head = NULL;
static struct devdir *tail = NULL;
static char pathname[_POSIX_PATH_MAX + 1] = {'\0'};

static void add(char *dirname)
{
    struct devdir *ddp = NULL;
    int len = 0;

    len = strlen(dirname);
    if (('.'==dirname[len-1]) &&
        ('/'==dirname[len-2] ||
        ('.'==dirname[len-2] && '/'==dirname[len-3]))) {
        return;
    }

    if (0 == strcmp(dirname, "/dev/fd")) {
        return;
    }

    ddp = malloc(sizeof(struct devdir));
    if (NULL == ddp) {
        return;
    }

    ddp->d_name = strdup(dirname);
    if (NULL == ddp->d_name) {
        free(ddp);
        return;
    }

    ddp->d_next = NULL;
    if (NULL == tail) {
        head = ddp;
        tail = ddp;
    } else {
        tail->d_next = ddp;
        tail = ddp;
    }
}

static void cleanup(void)
{
    struct devdir *ddp = NULL;
    struct devdir *nddp = NULL;

    ddp = head;
    while (NULL != ddp) {
        nddp = ddp->d_next;
        free(ddp->d_name);
        free(ddp);
        ddp = nddp;
    }

    head = NULL;
    tail = NULL;
}

static char * searchdir(char *dirname, struct stat *fdstatp)
{
    struct stat devstat;
    DIR *dp = NULL;
    int devlen = 0;
    struct dirent *dirp = NULL;

    strcpy(pathname, dirname);
    dp = opendir(dirname);

    if (NULL == dp) {
        return NULL;
    }

    strcat(pathname, "/");
    devlen = strlen(pathname);

    while (1) {
        dirp = readdir(dp);
        if (NULL == dirp) {
            break;
        }

        strncpy(pathname + devlen, dirp->d_name, _POSIX_PATH_MAX - devlen);
        if (0==strcmp(pathname, "/dev/stdin") ||
            0==strcmp(pathname, "/dev/stdout") ||
            0==strcmp(pathname, "/dev/stderr")) {
            continue;
        }

        if (stat(pathname, &devstat) < 0) {
            continue;
        }

        if (S_ISDIR(devstat.st_mode)) {
            add(pathname);
            continue;
        }

        if (devstat.st_ino==fdstatp->st_ino &&
            devstat.st_dev==fdstatp->st_dev) {
            closedir(dp);
            return pathname;
        }
    }

    closedir(dp);
    return NULL;
}

char *ttyname(int fd)
{
    struct stat fdstat;
    struct devdir *ddp = NULL;
    char *rval = NULL;

    if (0 == isatty(fd)) {
        return NULL;
    }

    if (fstat(fd, &fdstat) < 0) {
        return NULL;
    }

    if (0 == S_ISCHR(fdstat.st_mode)) {
        return NULL;
    }


    rval = searchdir("/dev", &fdstat);
    if (NULL == rval) {
        for (ddp=head; NULL!=ddp; ddp=ddp->d_next) {
            rval = searchdir(ddp->d_name, &fdstat);
            if (NULL != rval) {
                break;
            }
        }
    }

    cleanup();
    return rval;
}
