#include <nummeths.h>
#include <math.h>

int root(double (*f)(double x),
    double (*g)(double x),
    double *x, int *n, double delta)
{
    int satisfied = 0;
    int i = 0;
    
    while (!satisfied && i + 1 < *n) {
        x[i + 1] = x[i] - (f(x[i]) / g(x[i]));
        if (fabs(x[i + 1] - x[i]) < delta) {
            satisfied = 1;
        }

        i++;
    }
    
    if (0 == i) {
        *n = 1;
    } else {
        *n = i + 1;
    }
    
    if (0 != satisfied) {
        return 0;
    }
    
    return -1;
}
