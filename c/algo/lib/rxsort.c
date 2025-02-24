#include <sort.h>
#include <limits.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>

int rxsort(int *data, int size, int p, int k)
{
    int *counts = NULL;
    int *temp = NULL;
    int index = 0;
    int pval = 0;
    int i = 0;
    int j = 0;
    int n = 0;
    
    counts = (int *)malloc(k * sizeof(int));
    if (NULL == counts) {
        return -1;
    }
    
    temp = (int *)malloc(size * sizeof(int));
    if (NULL == temp) {
        return -1;
    }
    
    for (n=0; n<p; n++) {
        for (i=0; i<k; i++) {
            counts[i] = 0;
        }
    
        pval = (int)pow((double)k, (double)n);
        for (j=0; j<size; j++) {
            index = (int)(data[j] / pval) % k;
            counts[index] = counts[index] + 1;
        }
    
        for (i=1; i<k; i++) {
            counts[i] = counts[i] + counts[i - 1];
        }
    
        for (j=size-1; j>=0; j--) {
            index = (int)(data[j] / pval) % k;
            temp[counts[index] - 1] = data[j];
            counts[index] = counts[index] - 1;
        }
    
        memcpy(data, temp, size * sizeof(int));
    }
    
    free(counts);
    free(temp);
    return 0;
}
