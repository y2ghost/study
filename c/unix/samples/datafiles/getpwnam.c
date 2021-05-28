#include <pwd.h>
#include <stddef.h>
#include <string.h>

struct passwd *getpwnam(const char *name)
{
    struct passwd *ptr = NULL;

    setpwent();
    while (1) {
        ptr = getpwent();
        if (NULL == ptr) {
            break;
        }

        if (0 == strcmp(name,ptr->pw_name)) {
            break;
        }
    }

    endpwent();
    return ptr;
}
