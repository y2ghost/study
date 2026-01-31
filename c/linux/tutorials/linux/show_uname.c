#define _GNU_SOURCE
#include <sys/utsname.h>
#include <stdio.h>

int main(int ac, char *av[])
{
    struct utsname uts;
    if (-1 == uname(&uts)) {
        return 1;
    }

    printf("Node name:      %s\n", uts.nodename);
    printf("System name:    %s\n", uts.sysname);
    printf("Release:        %s\n", uts.release);
    printf("Version:        %s\n", uts.version);
    printf("Machine:        %s\n", uts.machine);
    printf("Domain name:    %s\n", uts.domainname);
    return 0;
}
