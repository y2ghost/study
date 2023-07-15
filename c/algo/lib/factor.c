#include "factor.h"
#include <math.h>
#include <stdio.h>

void factor(int number, int n, int j)
{
    int i = 0;
    
    if (1 == n) {
        printf("1 is a unit\n");
        return;
    }
    
    i = j;
    while (i <= (int)(sqrt((double)n))) {
        if (n % i == 0) {
            fprintf(stdout, "%d\n", i);
            factor(number, (int)(n / i), i);
            return;
        } else {
            i++;
        }
    }
    
    if (n == number) {
        printf("%d is prime\n", number);
    } else {
        printf("%d\n", n);
    }
}
