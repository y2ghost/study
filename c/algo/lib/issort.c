#include <sort.h>
#include <stdlib.h>
#include <string.h>

int issort(void *data, int size, int esize, compare cmp)
{
    void *key = (char *)malloc(esize);
    if (NULL == key) {
        return -1;
    }

    int i = 0;
    int j = 0;
    char *a = data;

    for (j=1; j<size; j++) {
        memcpy(key, a + j * esize, esize);
        i = j - 1;
    
        while (i>=0 && cmp(a+ i * esize, key) > 0) {
            memcpy(a + (i + 1) * esize, a + i * esize, esize);
            i--;
        }
    
        memcpy(a + (i + 1) * esize, key, esize);
    }
    
    free(key);
    return 0;
}
