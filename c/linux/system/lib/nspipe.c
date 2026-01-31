#include "common.h"
#include <sys/socket.h>
#include <sys/un.h>

int ns_pipe(const char *name, int fd[2])
{
    if (fd_pipe(fd) < 0)  {
        return -1;
    }

    unlink(name);
    struct sockaddr_un unix_addr;
    memset(&unix_addr, 0x0, sizeof(unix_addr));
    unix_addr.sun_family = AF_UNIX;
    strcpy(unix_addr.sun_path, name);
    int len = strlen(unix_addr.sun_path) + sizeof(unix_addr.sun_family);
    return bind(fd[0], (struct sockaddr *) &unix_addr, len);
}
