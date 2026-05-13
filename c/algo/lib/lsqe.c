#include <nummeths.h>
#include <math.h>

void lsqe(const double *x, const double *y, int n,
    double *b1, double *b0)
{
    double sumx = 0;
    double sumy = 0;
    double sumx2 = 0;
    double sumxy = 0;
    int i = 0;
    
    sumx = 0.0;
    sumy = 0.0;
    sumx2 = 0.0;
    sumxy = 0.0;
    
    for (i=0; i<n; i++) {
        sumx = sumx + x[i];
        sumy = sumy + y[i];
        sumx2 = sumx2 + pow(x[i], 2.0);
        sumxy = sumxy + (x[i] * y[i]);
    }
    
    *b1 = (sumxy - ((sumx * sumy)/(double)n)) / (sumx2-(pow(sumx,2.0)/(double)n));
    *b0 = (sumy - ((*b1) * sumx)) / (double)n;
}
