#include <hashpjw.h>
#include <stdlib.h>

int hashpjw(const void *key)
{
    const char *ptr = key;
    int val = 0;
    
    while (*ptr != '\0') {
        int tmp = 0;
        val = (val << 4) + (*ptr);
        tmp = val & 0xf0000000;

        if (0 != tmp) {
            val = val ^ (tmp >> 24);
            val = val ^ tmp;
        }
        
        ptr++;
    }
    
    return val % PRIME_TBLSIZ;
}
