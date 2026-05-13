#include <nummeths.h>
#include <stdlib.h>
#include <string.h>

int interpol(const double *x, const double *fx, int n,
    double *z, double *pz, int m)
{
    double term = 0;
    double *table = NULL;
    double *coeff = NULL;
    int i = 0;
    int j = 0;
    int k = 0;
    
    table = (double *)malloc(sizeof(double) * n);
    if (NULL == table) {
        return -1;
    }
    
    coeff = (double *)malloc(sizeof(double) * n);
    if (NULL == coeff) {
        free(table);
        return -1;
    }
    
    memcpy(table, fx, sizeof(double) * n);
    coeff[0] = table[0];
    
    for (k=1; k<n; k++) {
        for (i=0; i<n-k; i++) {
            j = i + k;
            table[i] = (table[i + 1] - table[i]) / (x[j] - x[i]);
        }
    
        coeff[k] = table[0];
    }
    
    free(table);
    for (k=0; k<m; k++) {
        pz[k] = coeff[0];
        for (j=1; j<n; j++) {
            term = coeff[j]; 
            for (i=0; i<j; i++) {
                term = term * (z[k] - x[i]);
            }
    
            pz[k] = pz[k] + term;
        }
    }
    
    free(coeff);
    return 0;
}
