#include "common.h"
#include <sys/socket.h>

#define CONTROLLEN  CMSG_LEN(sizeof(int))

static struct cmsghdr *cmptr = NULL;

int recv_fd(int fd, ssize_t (*userfunc)(int, const void *, size_t))
{
    if (NULL == cmptr) {
        cmptr = malloc(CONTROLLEN);
        if (NULL == cmptr) {
            return -1;
        }
    }

    int status = -1;
    struct msghdr msg;
    struct iovec iov[1];
    char buf[MAXLINE] = {'\0'};

    while (1) {
        iov[0].iov_base = buf;
        iov[0].iov_len = sizeof(buf);
        msg.msg_iov = iov;
        msg.msg_iovlen = 1;
        msg.msg_name = NULL;
        msg.msg_namelen = 0;
        msg.msg_control = cmptr;
        msg.msg_controllen = CONTROLLEN;

        int nr = recvmsg(fd, &msg, 0);
        if (nr < 0) {
            err_ret("recvmsg error");
            return -1;
        } else if (0 == nr) {
            err_ret("connection closed by server");
            return -1;
        }

        char *ptr = buf;
        int newfd = 0;

        while (ptr < &buf[nr]) {
            if (0 == *ptr++) {
                if (ptr != &buf[nr-1]) {
                    err_dump("message format error");
                }

                status = *ptr & 0xFF;
                if (0 == status) {
                    if (CONTROLLEN != msg.msg_controllen) {
                        err_dump("status = 0 but no fd");
                    }

                    newfd = *(int *)CMSG_DATA(cmptr);
                } else {
                    newfd = -status;
                }

                nr -= 2;
            }
        }

        if (nr>0 && userfunc(STDERR_FILENO,buf,nr)!=nr) {
            return -1;
        }

        if (status >= 0) {
            return newfd;
        }
    }

    return -1;
}
