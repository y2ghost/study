#ifndef INOTIFY_UTILS_H_
#define INOTIFY_UTILS_H_

#include "event_queue.h"

void handle_event(queue_entry_t event);
int read_event(int fd, struct inotify_event *event);
int event_check(int fd);
int process_inotify_events(queue_t q, int fd);
int watch_dir(int fd, const char *dirname, unsigned long mask);
int ignore_wd(int fd, int wd);
int close_inotify_fd(int fd);
int open_inotify_fd();

#endif /* INOTIFY_UTILS_H_ */
