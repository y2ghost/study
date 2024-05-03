#include "common.h"
#include <sys/inotify.h>
#include <limits.h>

#define BUF_LEN (10 * (sizeof(struct inotify_event) + NAME_MAX + 1))

static void displayInotifyEvent(struct inotify_event *i)
{
    printf("    wd =%2d; ", i->wd);
    if (i->cookie > 0) {
        printf("cookie =%4d; ", i->cookie);
    }

    printf("mask = ");
    if (i->mask & IN_ACCESS)        { printf("IN_ACCESS "); }
    if (i->mask & IN_ATTRIB)        { printf("IN_ATTRIB "); }
    if (i->mask & IN_CLOSE_NOWRITE) { printf("IN_CLOSE_NOWRITE "); }
    if (i->mask & IN_CLOSE_WRITE)   { printf("IN_CLOSE_WRITE "); }
    if (i->mask & IN_CREATE)        { printf("IN_CREATE "); }
    if (i->mask & IN_DELETE)        { printf("IN_DELETE "); }
    if (i->mask & IN_DELETE_SELF)   { printf("IN_DELETE_SELF "); }
    if (i->mask & IN_IGNORED)       { printf("IN_IGNORED "); }
    if (i->mask & IN_ISDIR)         { printf("IN_ISDIR "); }
    if (i->mask & IN_MODIFY)        { printf("IN_MODIFY "); }
    if (i->mask & IN_MOVE_SELF)     { printf("IN_MOVE_SELF "); }
    if (i->mask & IN_MOVED_FROM)    { printf("IN_MOVED_FROM "); }
    if (i->mask & IN_MOVED_TO)      { printf("IN_MOVED_TO "); }
    if (i->mask & IN_OPEN)          { printf("IN_OPEN "); }
    if (i->mask & IN_Q_OVERFLOW)    { printf("IN_Q_OVERFLOW "); }
    if (i->mask & IN_UNMOUNT)       { printf("IN_UNMOUNT "); }
    printf("\n");

    if (i->len > 0) {
        printf("        name = %s\n", i->name);
    }
}

int main(int argc, char *argv[])
{
    if (argc < 2 || strcmp(argv[1], "--help") == 0) {
        err_quit("%s pathname...\n", argv[0]);
    }

    int inotifyFd = inotify_init();
    if (inotifyFd == -1) {
        err_quit("inotify_init");
    }

    for (int j = 1; j < argc; j++) {
        int wd = inotify_add_watch(inotifyFd, argv[j], IN_ALL_EVENTS);
        if (wd == -1) {
            err_quit("inotify_add_watch");
        }

        printf("Watching %s using wd %d\n", argv[j], wd);
    }

    char buf[BUF_LEN] = {0};
    for (;;) {
        ssize_t numRead = read(inotifyFd, buf, BUF_LEN);
        if (numRead == 0) {
            err_quit("read() from inotify fd returned 0!");
        }

        if (numRead == -1) {
            err_quit("read");
        }

        printf("Read %ld bytes from inotify fd\n", (long) numRead);
        for (char *p = buf; p < buf + numRead; ) {
            struct inotify_event *event = (struct inotify_event *) p;
            displayInotifyEvent(event);
            p += sizeof(struct inotify_event) + event->len;
        }
    }

    exit(EXIT_SUCCESS);
}

