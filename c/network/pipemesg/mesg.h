#ifndef PIPE_MESG_H
#define PIPE_MESG_H

#include <unistd.h>
#include <limits.h>
#include <sys/types.h>

#define MAXMESGDATA (PIPE_BUF-2*sizeof(long))
#define MESGHDRSIZE (sizeof(struct mymesg)-MAXMESGDATA)

struct mymesg {
    long len;
    long type;
    char data[MAXMESGDATA];
};

ssize_t mesg_send(int fd, struct mymesg*);
ssize_t mesg_recv(int fd, struct mymesg*);

#endif /* PIPE_MESG_H */
