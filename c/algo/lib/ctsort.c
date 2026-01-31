#include <sort.h>
#include <stdlib.h>
#include <string.h>

int ctsort(int *data, int size, int k)
{
    int *counts = NULL;
    int *temp = NULL;
    int i = 0;
    int j = 0;
    
    counts = (int *)malloc(k * sizeof(int));
    if (NULL == counts) {
        return -1;
    }
    
    temp = (int *)malloc(size * sizeof(int));
    if (NULL == temp) {
        return -1;
    }
    
    for (i=0; i<k; i++) {
        counts[i] = 0;
    }
    
    for (j=0; j<size; j++) {
        counts[data[j]] = counts[data[j]] + 1;
    }
    
    for (i=1; i<k; i++) {
        counts[i] = counts[i] + counts[i - 1];
    }
    
    for (j=size-1; j>=0; j--) {
        temp[counts[data[j]] - 1] = data[j];
        counts[data[j]] = counts[data[j]] - 1;
    }
    
    memcpy(data, temp, size * sizeof(int));
    free(counts);
    free(temp);
    return 0;
}
