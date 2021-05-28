#include "apue.h"
#include <sys/socket.h>
#include <sys/un.h>

int ns_pipe(const char *name, int fd[2])
{
    int len;
    struct sockaddr_un unix_addr;

    if (fd_pipe(fd) < 0)  {
        return -1;
    }

    unlink(name);
    memset(&unix_addr, 0x0, sizeof(unix_addr));
    unix_addr.sun_family = AF_UNIX;
    strcpy(unix_addr.sun_path, name);
    len = strlen(unix_addr.sun_path) + sizeof(unix_addr.sun_family);
    return bind(fd[0], (struct sockaddr *) &unix_addr, len);
}
