#include <hash.h>

unsigned int string_hash_make(const char *str)
{
    unsigned int hash = 0;
    while (*str) {
        hash = (hash*33) ^ (unsigned int)*str++;
    }

    return hash;
}
