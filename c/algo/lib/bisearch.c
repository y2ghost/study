#include "search.h"
#include <stdlib.h>
#include <string.h>

int bisearch(void *sorted, const void *target, int size, int esize, int
    (*compare)(const void *key1, const void *key2))
{
    int left = 0;
    int middle = 0;
    int right = 0;
    
    left = 0;
    right = size - 1;
    
    while (left <= right) {
        middle = (left + right) / 2;
        switch (compare(((char *)sorted + (esize * middle)), target)) {
        case -1:
            left = middle + 1;
            break;
        case 1:
            right = middle - 1;
            break;
        case 0:
            return middle;
       }
    }
    
    return -1;
}
