#include <sys/select.h>
#include <sys/stat.h>
#include <limits.h>
#include <sys/select.h>
#include <sys/inotify.h>
#include <fcntl.h>
#include <ftw.h>
#include <signal.h>
#include <stdarg.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>

#define errExit(msg)    do { perror(msg); exit(EXIT_FAILURE); \
                        } while (0)

#define VB_BASIC 1
#define VB_NOISY 2

static int verboseMask = 0;
static int checkCache = 0;
static int dumpCache = 0;
static int readBufferSize = 0;
static char *stopFile = NULL;
static int abortOnCacheProblem = 0;
static FILE *logfp = NULL;
static int inotifyReadCnt = 0;

static const int INOTIFY_READ_BUF_LEN =
    (100 * (sizeof(struct inotify_event) + NAME_MAX + 1));

static void dumpCacheToLog(void);

static void createStopFileAndAbort(void)
{
    open(stopFile, O_CREAT | O_RDWR, 0600);
    dumpCacheToLog();
    abort();
}

static void logMessage(int vb_mask, const char *format, ...)
{
    va_list argList;
    if ((vb_mask == 0) || (vb_mask & verboseMask)) {
        va_start(argList, format);
        vfprintf(stderr, format, argList);
        va_end(argList);
    }

    if (logfp != NULL) {
        va_start(argList, format);
        vfprintf(logfp, format, argList);
        va_end(argList);
    }
}


static void displayInotifyEvent(struct inotify_event *ev)
{
    logMessage(VB_NOISY, "==> wd = %d; ", ev->wd);
    if (ev->cookie > 0) {
        logMessage(VB_NOISY, "cookie = %4d; ", ev->cookie);
    }

    logMessage(VB_NOISY, "mask = ");
    if (ev->mask & IN_ISDIR) { 
        logMessage(VB_NOISY, "IN_ISDIR ");
    }

    if (ev->mask & IN_CREATE) {
        logMessage(VB_NOISY, "IN_CREATE ");
    }

    if (ev->mask & IN_DELETE_SELF) {
        logMessage(VB_NOISY, "IN_DELETE_SELF ");
    }

    if (ev->mask & IN_MOVE_SELF) {
        logMessage(VB_NOISY, "IN_MOVE_SELF ");
    }

    if (ev->mask & IN_MOVED_FROM) {
        logMessage(VB_NOISY, "IN_MOVED_FROM ");
    }

    if (ev->mask & IN_MOVED_TO) {
        logMessage(VB_NOISY, "IN_MOVED_TO ");
    }

    if (ev->mask & IN_IGNORED) {
        logMessage(VB_NOISY, "IN_IGNORED ");
    }

    if (ev->mask & IN_Q_OVERFLOW) {
        logMessage(VB_NOISY, "IN_Q_OVERFLOW ");
    }

    if (ev->mask & IN_UNMOUNT) {
        logMessage(VB_NOISY, "IN_UNMOUNT ");
    }

    logMessage(VB_NOISY, "\n");
    if (ev->len > 0) {
        logMessage(VB_NOISY, "        name = %s\n", ev->name);
    }
}


struct watch {
    int wd;
    char path[PATH_MAX];
};

struct watch *wlCache = NULL;

static int cacheSize = 0;

static void freeCache(void)
{
    free(wlCache);
    cacheSize = 0;
    wlCache = NULL;
}

static void checkCacheConsistency(void)
{
    struct stat sb;
    int failures = 0;

    for (int j = 0; j < cacheSize; j++) {
        if (wlCache[j].wd >= 0) {
            if (lstat(wlCache[j].path, &sb) == -1) {
                logMessage(0,
                        "checkCacheConsistency: stat: "
                        "[slot = %d; wd = %d] %s: %s\n",
                        j, wlCache[j].wd, wlCache[j].path, strerror(errno));
                failures++;
            }
        } else if (!S_ISDIR(sb.st_mode)) {
            logMessage(0, "checkCacheConsistency: %s is not a directory\n",
                wlCache[j].path);
            exit(EXIT_FAILURE);
        }
    }

    if (failures > 0) {
        logMessage(VB_NOISY, "checkCacheConsistency: %d failures\n", failures);
    }
}

static int findWatch(int wd)
{
    for (int j = 0; j < cacheSize; j++) {
        if (wlCache[j].wd == wd) {
            return j;
        }
    }

    return -1;
}

static int findWatchChecked(int wd)
{
    int slot = findWatch(wd);
    if (slot >= 0) {
        return slot;
    }

    logMessage(0, "Could not find watch %d\n", wd);
    if (abortOnCacheProblem) {
        createStopFileAndAbort();
    }

    return -1;
}

static void markCacheSlotEmpty(int slot)
{
    logMessage(VB_NOISY,
            "        markCacheSlotEmpty: slot = %d;  wd = %d; path = %s\n",
            slot, wlCache[slot].wd, wlCache[slot].path);
    wlCache[slot].wd = -1;
    wlCache[slot].path[0] = '\0';
}

static int findEmptyCacheSlot(void)
{
    const int ALLOC_INCR = 200;
    for (int j = 0; j < cacheSize; j++) {
        if (wlCache[j].wd == -1) {
            return j;
        }
    }

    cacheSize += ALLOC_INCR;
    wlCache = realloc(wlCache, cacheSize * sizeof(struct watch));

    if (wlCache == NULL) {
        errExit("realloc");
    }

    for (int j = cacheSize - ALLOC_INCR; j < cacheSize; j++) {
        markCacheSlotEmpty(j);
    }

    return cacheSize - ALLOC_INCR;
}

static int addWatchToCache(int wd, const char *pathname)
{
    int slot = findEmptyCacheSlot();
    wlCache[slot].wd = wd;
    strncpy(wlCache[slot].path, pathname, PATH_MAX);
    return slot;
}

static int pathnameToCacheSlot(const char *pathname)
{
    for (int j = 0; j < cacheSize; j++) {
        if (wlCache[j].wd >= 0 && strcmp(wlCache[j].path, pathname) == 0) {
            return j;
        }
    }

    return -1;
}

static int pathnameInCache(const char *pathname)
{
    return pathnameToCacheSlot(pathname) >= 0;
}

static void dumpCacheToLog(void)
{
    int cnt = 0;
    for (int j = 0; j < cacheSize; j++) {
        if (wlCache[j].wd >= 0) {
            fprintf(logfp, "%d: wd = %d; %s\n", j,
                    wlCache[j].wd, wlCache[j].path);
            cnt++;
        }
    }

    fprintf(logfp, "Total entries: %d\n", cnt);
}

static char **rootDirPaths = NULL;
static int numRootDirs = 0;
static int ignoreRootDirs = 0;
static struct stat *rootDirStat = NULL;

static void copyRootDirPaths(char *argv[])
{
    numRootDirs = 0;
    for (char **p = argv; *p != NULL; p++) {
        struct stat sb;
        if (lstat(*p, &sb) == -1) {
            fprintf(stderr, "lstat() failed on '%s'\n", *p);
            exit(EXIT_FAILURE);
        }

        if (! S_ISDIR(sb.st_mode)) {
            fprintf(stderr, "'%s' is not a directory\n", *p);
            exit(EXIT_FAILURE);
        }

        numRootDirs++;
    }

    rootDirPaths = calloc(numRootDirs, sizeof(char *));
    if (rootDirPaths == NULL) {
        errExit("calloc");
    }

    rootDirStat = calloc(numRootDirs, sizeof(struct stat));
    if (rootDirPaths == NULL) {
        errExit("calloc");
    }

    for (int j = 0; j < numRootDirs; j++) {
        rootDirPaths[j] = strdup(argv[j]);
        if (rootDirPaths[j] == NULL) {
            errExit("strdup");
        }

        if (lstat(argv[j], &rootDirStat[j]) == -1) {
            errExit("lstat");
        }

        for (int k = 0; k < j; k++) {
            if ((rootDirStat[j].st_ino == rootDirStat[k].st_ino) &&
                (rootDirStat[j].st_dev == rootDirStat[k].st_dev)) {
                fprintf(stderr, "Duplicate filesystem objects: %s, %s\n",
                        argv[j], argv[k]);
                exit(EXIT_FAILURE);
            }
        }
    }

    ignoreRootDirs = 0;
}

static char **findRootDirPath(const char *path)
{
    for (int j = 0; j < numRootDirs; j++) {
        if (rootDirPaths[j] != NULL && strcmp(path, rootDirPaths[j]) == 0) {
            return &rootDirPaths[j];
        }
    }


    return NULL;
}

static int isRootDirPath(const char *path)
{
    return findRootDirPath(path) != NULL;
}

static void zapRootDirPath(const char *path)
{
    printf("zapRootDirPath: %s\n", path);
    char **p = findRootDirPath(path);

    if (p == NULL) {
        fprintf(stderr, "zapRootDirPath(): path not found!\n");
        exit(EXIT_FAILURE);
    }

    *p = NULL;
    ignoreRootDirs++;

    if (ignoreRootDirs == numRootDirs) {
        fprintf(stderr, "No more root paths left to monitor; bye!\n");
        exit(EXIT_SUCCESS);
    }
}

static int dirCnt = 0;
static int ifd = 0;

static int traverseTree(const char *pathname, const struct stat *sb,
    int tflag, struct FTW *ftwbuf)
{
    if (! S_ISDIR(sb->st_mode)) {
        return 0;
    }

    int flags = IN_CREATE | IN_MOVED_FROM | IN_MOVED_TO | IN_DELETE_SELF;
    if (isRootDirPath(pathname)) {
        flags |= IN_MOVE_SELF;
    }

    int wd = inotify_add_watch(ifd, pathname, flags | IN_ONLYDIR);
    if (wd == -1) {
        logMessage(VB_BASIC, "inotify_add_watch: %s: %s\n",
                pathname, strerror(errno));
        if (errno == ENOENT) {
            return 0;
        } else {
            exit(EXIT_FAILURE);
        }
    }

    if (findWatch(wd) >= 0) {
        logMessage(VB_BASIC, "WD %d already in cache (%s)\n", wd, pathname);
        return 0;
    }

    dirCnt++;
    int slot = addWatchToCache(wd, pathname);
    logMessage(VB_NOISY, "    watchDir: wd = %d [cache slot: %d]; %s\n",
        wd, slot, pathname);
    return 0;
}

static int watchDir(int inotifyFd, const char *pathname)
{
    dirCnt = 0;
    ifd = inotifyFd;

    if (nftw(pathname, traverseTree, 20, FTW_PHYS) == -1) {
        logMessage(VB_BASIC,
                "nftw: %s: %s (directory probably deleted before we "
                "could watch)\n", pathname, strerror(errno));
    }

    return dirCnt;
}

static void watchSubtree(int inotifyFd, char *path)
{
    int cnt = watchDir(inotifyFd, path);
    logMessage(VB_BASIC, "    watchSubtree: %s: %d entries added\n",
            path, cnt);
}

static void rewriteCachedPaths(const char *oldPathPrefix, const char *oldName,
    const char *newPathPrefix, const char *newName)
{
    char fullPath[2 * PATH_MAX] = {0};
    char newPrefix[2 * PATH_MAX] = {0};
    snprintf(fullPath, sizeof(fullPath), "%s/%s", oldPathPrefix, oldName);
    snprintf(newPrefix, sizeof(newPrefix), "%s/%s", newPathPrefix, newName);
    size_t len = strlen(fullPath);
    logMessage(VB_BASIC, "Rename: %s ==> %s\n", fullPath, newPrefix);

    for (int j = 0; j < cacheSize; j++) {
        if (strncmp(fullPath, wlCache[j].path, len) == 0 &&
                    (wlCache[j].path[len] == '/' ||
                     wlCache[j].path[len] == '\0')) {
            char newPath[PATH_MAX];
            int s = snprintf(newPath, sizeof(newPath), "%s%s", newPrefix,
                    &wlCache[j].path[len]);

            if (s > sizeof(newPath)) {
                logMessage(VB_BASIC, "Truncated pathname: %s\n", newPath);
            }

            strncpy(wlCache[j].path, newPath, PATH_MAX);
            logMessage(VB_NOISY, "    wd %d [cache slot %d] ==> %s\n",
                    wlCache[j].wd, j, newPath);
        }
    }
}

static int zapSubtree(int inotifyFd, char *path)
{
    logMessage(VB_NOISY, "Zapping subtree: %s\n", path);
    size_t len = strlen(path);
    char *pn = strdup(path);
    int cnt = 0;

    for (int j = 0; j < cacheSize; j++) {
        if (wlCache[j].wd >= 0) {
            if (strncmp(pn, wlCache[j].path, len) == 0 &&
                    (wlCache[j].path[len] == '/' ||
                     wlCache[j].path[len] == '\0')) {
                logMessage(VB_NOISY,
                           "    removing watch: wd = %d (%s)\n",
                           wlCache[j].wd, wlCache[j].path);

                if (inotify_rm_watch(inotifyFd, wlCache[j].wd) == -1) {
                    logMessage(0, "inotify_rm_watch wd = %d (%s): %s\n",
                            wlCache[j].wd, wlCache[j].path, strerror(errno));
                    cnt = -1;
                    break;
                }

                markCacheSlotEmpty(j);
                cnt++;
            }
        }
    }

    free(pn);
    return cnt;
}

static int reinitialize(int oldInotifyFd)
{
    static int reinitCnt = 0;
    if (oldInotifyFd >= 0) {
        close(oldInotifyFd);
        reinitCnt++;
        logMessage(0, "Reinitializing cache and inotify FD (reinitCnt = %d)\n",
                reinitCnt);
    } else {
        logMessage(0, "Initializing cache\n");
        reinitCnt = 0;
    }

    int inotifyFd = inotify_init();
    if (inotifyFd == -1) {
        errExit("inotify_init");
    }

    logMessage(VB_BASIC, "    new inotifyFd = %d\n", inotifyFd);
    freeCache();

    for (int j = 0; j < numRootDirs; j++) {
        if (rootDirPaths[j] != NULL) {
            watchSubtree(inotifyFd, rootDirPaths[j]);
        }
    }

    int cnt = 0;
    for (int j = 0; j < cacheSize; j++) {
        if (wlCache[j].wd >= 0) {
            cnt++;
        }
    }

    if (oldInotifyFd >= 0) {
        logMessage(0, "Rebuilt cache with %d entries\n", cnt);
    }

    return inotifyFd;
}

static size_t processNextInotifyEvent(int *inotifyFd, char *buf, int bufSize, int firstTry)
{
    char fullPath[PATH_MAX + NAME_MAX] = {0};
    int evCacheSlot = 0;
    struct inotify_event *ev = (struct inotify_event *) buf;
    displayInotifyEvent(ev);

    if (ev->wd != -1 && !(ev->mask & IN_IGNORED)) {
        evCacheSlot = findWatchChecked(ev->wd);
        if (evCacheSlot == -1) {
            *inotifyFd = reinitialize(*inotifyFd);
            return INOTIFY_READ_BUF_LEN;
        }
    }

    size_t evLen = sizeof(struct inotify_event) + ev->len;
    if ((ev->mask & IN_ISDIR) &&
            (ev->mask & (IN_CREATE | IN_MOVED_TO))) {
        snprintf(fullPath, sizeof(fullPath), "%s/%s",
                 wlCache[evCacheSlot].path, ev->name);
        logMessage(VB_BASIC, "Directory creation on wd %d: %s\n",
                ev->wd, fullPath);

        if (!pathnameInCache(fullPath)) {
            watchSubtree(*inotifyFd, fullPath);
        }
    } else if (ev->mask & IN_DELETE_SELF) {
        logMessage(VB_BASIC, "Clearing watchlist item %d (%s)\n",
            ev->wd, wlCache[evCacheSlot].path);
        if (isRootDirPath(wlCache[evCacheSlot].path)) {
            zapRootDirPath(wlCache[evCacheSlot].path);
        }

        markCacheSlotEmpty(evCacheSlot);
    } else if ((ev->mask & (IN_MOVED_FROM | IN_ISDIR)) ==
               (IN_MOVED_FROM | IN_ISDIR)) {
        struct inotify_event *nextEv = (struct inotify_event *) (buf + evLen);
        if (((char *) nextEv < buf + bufSize) &&
                (nextEv->mask & IN_MOVED_TO) &&
                (nextEv->cookie == ev->cookie)) {
            int nextEvCacheSlot = findWatchChecked(nextEv->wd);
            if (nextEvCacheSlot == -1) {
                *inotifyFd = reinitialize(*inotifyFd);
                return INOTIFY_READ_BUF_LEN;
            }

            rewriteCachedPaths(wlCache[evCacheSlot].path, ev->name,
                wlCache[nextEvCacheSlot].path, nextEv->name);
            evLen += sizeof(struct inotify_event) + nextEv->len;
        } else if (((char *) nextEv < buf + bufSize) || !firstTry) {
            logMessage(VB_NOISY, "MOVED_OUT: %p %p\n",
                wlCache[evCacheSlot].path, ev->name);
            logMessage(VB_NOISY, "firstTry = %d; remaining bytes = %d\n",
                firstTry, buf + bufSize - (char *) nextEv);
            snprintf(fullPath, sizeof(fullPath), "%s/%s",
                wlCache[evCacheSlot].path, ev->name);

            if (zapSubtree(*inotifyFd, fullPath) == -1) {
                *inotifyFd = reinitialize(*inotifyFd);
                return INOTIFY_READ_BUF_LEN;
            }
        } else {
            logMessage(VB_NOISY, "HANGING IN_MOVED_FROM\n");
            return -1;
        }
    } else if (ev->mask & IN_Q_OVERFLOW) {
        static int overflowCnt = 0;
        overflowCnt++;
        logMessage(0, "Queue overflow (%d) (inotifyReadCnt = %d)\n",
                    overflowCnt, inotifyReadCnt);
        *inotifyFd = reinitialize(*inotifyFd);
        evLen = INOTIFY_READ_BUF_LEN;
    } else if (ev->mask & IN_UNMOUNT) {
        logMessage(0, "Filesystem unmounted: %s\n",
            wlCache[evCacheSlot].path);
        markCacheSlotEmpty(evCacheSlot);
    } else if (ev->mask & IN_MOVE_SELF &&
            isRootDirPath(wlCache[evCacheSlot].path)) {
        logMessage(0, "Root path moved: %s\n",
            wlCache[evCacheSlot].path);
        zapRootDirPath(wlCache[evCacheSlot].path);

        if (zapSubtree(*inotifyFd, wlCache[evCacheSlot].path) == -1) {
            *inotifyFd = reinitialize(*inotifyFd);
            return INOTIFY_READ_BUF_LEN;
        }
    }

    if (checkCache) {
        checkCacheConsistency();
    }

    if (dumpCache) {
        dumpCacheToLog();
    }

    return evLen;
}

static void alarmHandler(int sig)
{
    return;
}

static void processInotifyEvents(int *inotifyFd)
{
    char buf[INOTIFY_READ_BUF_LEN];
    struct sigaction sa;
    sigemptyset(&sa.sa_mask);
    sa.sa_handler = alarmHandler;
    sa.sa_flags = 0;

    if (sigaction(SIGALRM, &sa, NULL) == -1) {
        errExit("sigaction");
    }

    int firstTry = 1;
    size_t cnt = (readBufferSize > 0) ? readBufferSize : INOTIFY_READ_BUF_LEN;
    ssize_t numRead = read(*inotifyFd, buf, cnt);

    if (numRead == -1) {
        errExit("read");
    }

    if (numRead == 0) {
        fprintf(stderr, "read() from inotify fd returned 0!");
        exit(EXIT_FAILURE);
    }

    inotifyReadCnt++;
    logMessage(VB_NOISY,
        "\n==========> Read %d: got %zd bytes\n",
        inotifyReadCnt, numRead);

    for (char *evp = buf; evp < buf + numRead; ) {
        int evLen = processNextInotifyEvent(inotifyFd, evp,
            buf + numRead - evp, firstTry);
        if (evLen > 0) {
            evp += evLen;
            firstTry = 1;
        } else {
            firstTry = 0;
            numRead = buf + numRead - evp;

            for (int j = 0; j < numRead; j++) {
                buf[j] = evp[j]; 
            }

            ualarm(2000, 0);
            ssize_t nr = read(*inotifyFd, buf + numRead,
                INOTIFY_READ_BUF_LEN - numRead);
            int savedErrno = errno;
            ualarm(0, 0);
            errno = savedErrno;
 
            if (nr == -1 && errno != EINTR) {
                errExit("read");
            }

            if (nr == 0) {
                fprintf(stderr, "read() from inotify fd returned 0!");
                exit(EXIT_FAILURE);
            }

            if (errno != -1) {
                numRead += nr;
                inotifyReadCnt++;
                logMessage(VB_NOISY,
                    "\n==========> SECONDARY Read %d: got %zd bytes\n",
                    inotifyReadCnt, nr);
            } else {
                logMessage(VB_NOISY,
                    "\n==========> SECONDARY Read got nothing\n");
            }

            evp = buf;
        }
    }
}

static void executeCommand(int *inotifyFd)
{
    const int MAX_LINE = 100;
    char line[MAX_LINE], arg[MAX_LINE];
    int cnt, failures;
    FILE *fp;

    ssize_t numRead = read(STDIN_FILENO, line, MAX_LINE);
    if (numRead <= 0) {
        printf("bye!\n");
        exit(EXIT_FAILURE);
    }

    line[numRead - 1] = '\0';
    if (strlen(line) == 0) {
        return;
    }

    char cmd;
    int ns = sscanf(line, "%c %s\n", &cmd, arg);

    switch (cmd) {
    case 'a':
        cnt = zapSubtree(*inotifyFd, arg);
        if (cnt == 0) {
            logMessage(VB_BASIC, "Adding new subtree: %s\n", arg);
        } else {
            logMessage(VB_BASIC, "Zapped: %s, %d entries\n", arg, cnt);
        }

        watchSubtree(*inotifyFd, arg);
        break;
    case 'c':
    case 'C':
        cnt = 0;
        failures = 0;
        for (int j = 0; j < cacheSize; j++) {
            if (wlCache[j].wd >= 0) {
                struct stat sb;
                if (lstat(wlCache[j].path, &sb) == -1) {
                    if (cmd == 'c') {
                        logMessage(VB_BASIC,
                                "stat: [slot = %d; wd = %d] %s: %s\n",
                                j, wlCache[j].wd, wlCache[j].path,
                                strerror(errno));
                    }
                    failures++;
                } else if (!S_ISDIR(sb.st_mode)) {
                    if (cmd == 'c') {
                        logMessage(0, "%s is not a directory\n",
                            wlCache[j].path);
                    }

                    exit(EXIT_FAILURE);
                } else {
                    if (cmd == 'c') {
                        logMessage(VB_NOISY,
                            "OK: [slot = %d; wd = %d] %s\n",
                            j, wlCache[j].wd, wlCache[j].path);
                    }

                    cnt++;
                }
            }
        }

        logMessage(0, "Successfully verified %d entries\n", cnt);
        logMessage(0, "Failures: %d\n", failures);
        break;
    case 'l':
        cnt = 0;
        for (int j = 0; j < cacheSize; j++) {
            if (wlCache[j].wd >= 0) {
                logMessage(0, "%d: %d %s\n", j, wlCache[j].wd,
                           wlCache[j].path);
                cnt++;
            }
        }

        logMessage(VB_BASIC, "Total entries: %d\n", cnt);
        break;
    case 'q':
        exit(EXIT_SUCCESS);
    case 'v':
        if (ns == 2) {
            verboseMask = atoi(arg);
        } else {
            verboseMask = !verboseMask;
            printf("%s\n", verboseMask ? "on" : "off");
        }

        break;

    case 'd':
        dumpCache = !dumpCache;
        printf("%s\n", dumpCache ? "on" : "off");
        break;
    case 'x':
        checkCache = !checkCache;
        printf("%s\n", checkCache ? "on" : "off");
        break;
    case 'w':
        fp = fopen(arg, "w+");
        if (fp == NULL) {
            perror("fopen");
        }

        for (int j = 0; j < cacheSize; j++) {
            if (wlCache[j].wd >= 0) {
                fprintf(fp, "%s\n", wlCache[j].path);
            }
        }

        fclose(fp);
        break;
    case 'z':
        cnt = zapSubtree(*inotifyFd, arg);
        logMessage(VB_BASIC, "Zapped: %s, %d entries\n", arg, cnt);
        break;
    case '0':
        close(*inotifyFd);
        *inotifyFd = reinitialize(-1);
        break;
    default:
        printf("Unrecognized command: %c\n", cmd);
        printf("Commands:\n");
        printf("0        Rebuild cache\n");
        printf("a path   Add/refresh pathname watches and cache\n");
        printf("c        Verify cached pathnames\n");
        printf("d        Toggle cache dumping\n");
        printf("l        List cached pathnames\n");
        printf("q        Quit\n");
        printf("v [n]    Toggle/set verbose level for messages to stderr\n");
        printf("             0 = no messages\n");
        printf("             1 = basic messages\n");
        printf("             2 = verbose messages\n");
        printf("             3 = basic and verbose messages\n");
        printf("w file   Write directory list to file\n");
        printf("x        Toggle cache checking\n");
        printf("z path   Zap pathname and watches from cache\n");
        break;
    }
}

static void usageError(const char *pname)
{
    fprintf(stderr, "Usage: %s [options] directory-path\n\n", pname);
    fprintf(stderr, "    -v lvl   Display logging information\n");
    fprintf(stderr, "    -l file  Send logging information to a file\n");
    fprintf(stderr, "    -x       Check cache consistency after each "
        "operation\n");
    fprintf(stderr, "    -d       Dump cache to log after every operation\n");
    fprintf(stderr, "    -b size  Set buffer size for read() from "
        "inotify FD\n");
    fprintf(stderr, "    -a file  Abort when cache inconsistency detected, "
        "and create 'stop' file\n");
    exit(EXIT_FAILURE);
}

int main(int argc, char *argv[])
{
    verboseMask = 0;
    checkCache = 0;
    dumpCache = 0;
    stopFile = NULL;
    abortOnCacheProblem = 0;
    int opt = 0;

    while ((opt = getopt(argc, argv, "a:dxl:v:b:")) != -1) {
        switch (opt) {
        case 'a':
            abortOnCacheProblem = 1;
            stopFile = optarg;
            break;
        case 'x':
            checkCache = 1;
            break;
        case 'd':
            dumpCache = 1;
            break;
        case 'v':
            verboseMask = atoi(optarg);
            break;
        case 'b':
            readBufferSize = atoi(optarg);
            break;
        case 'l':
            logfp = fopen(optarg, "w+");
            if (logfp == NULL) {
                errExit("fopen");
            }

            setbuf(logfp, NULL);
            break;

        default:
            usageError(argv[0]);
        }
    }

    if (optind >= argc) {
        usageError(argv[0]);
    }

    copyRootDirPaths(&argv[optind]);
    int inotifyFd = reinitialize(-1);
    printf("%s> ", argv[0]);
    fflush(stdout);

    for (;;) {
        fd_set rfds;
        FD_ZERO(&rfds);
        FD_SET(STDIN_FILENO, &rfds);
        FD_SET(inotifyFd, &rfds);

        if (select(inotifyFd + 1, &rfds, NULL, NULL, NULL) == -1) {
            errExit("select");
        }

        if (FD_ISSET(STDIN_FILENO, &rfds)) {
            executeCommand(&inotifyFd);
            printf("%s> ", argv[0]);
            fflush(stdout);
        }

        if (FD_ISSET(inotifyFd, &rfds)) {
            processInotifyEvents(&inotifyFd);
        }
    }

    exit(EXIT_SUCCESS);
}
