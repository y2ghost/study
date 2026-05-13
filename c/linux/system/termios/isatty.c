#include <termios.h>

int isatty(int fd)
{
    struct termios ts;

    return (-1 != tcgetattr(fd, &ts));
}
