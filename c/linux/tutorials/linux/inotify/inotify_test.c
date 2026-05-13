#include "event_queue.h"
#include "inotify_utils.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/select.h>
#include <sys/inotify.h>
#include <signal.h>

int keep_running = 0;

/* This program will take as arguments one or more directory 
   or file names, and monitor them, printing event notifications 
   to the console. It will automatically terminate if all watched
   items are deleted or unmounted. Use ctrl-C or kill to 
   terminate otherwise.
*/

/* Signal handler that simply resets a flag to cause termination */
void signal_handler(int signum)
{
    keep_running = 0;
}

int main(int argc, char **argv)
{
    /* This is the file descriptor for the inotify watch */
    int inotify_fd = 0; 

    /* Set a ctrl-c signal handler */
    keep_running = 1;
    if (signal(SIGINT, signal_handler) == SIG_IGN) {
        /* Reset to SIG_IGN(ignore) if that was the prior state */
        signal(SIGINT, SIG_IGN);
    }
    
    /* First we open the inotify dev entry */
    inotify_fd = open_inotify_fd();
    if (inotify_fd > 0) {
        int wd = 0;
        int index = 0;
        queue_t q = NULL;
        
        q = queue_create(128);
        wd = 0;
        printf("\n");
        
        for (index=1;(index<argc)&&(wd>=0); index++) {
            wd = watch_dir(inotify_fd, argv[index], IN_ALL_EVENTS);
        }
        
        if (wd > 0) {
            process_inotify_events(q, inotify_fd);
        }

        printf("\nTerminating\n");
        close_inotify_fd(inotify_fd);
        queue_destroy(q);
    }

    return 0;
}
