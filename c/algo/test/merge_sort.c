#include <stdio.h>

int merge(int a[], int m, int b[], int n, int c[])
{
    int i = 0;
    int j = 0;
    int k = -1;

    while (i<m || j<n) {
        if (i == m) {
            c[++k] = b[j++];
        } else if (j == n) {
            c[++k] = a[i++];
        } else if (a[i] < b[j]) {
            c[++k] = a[i++];
        } else {
            c[++k] = b[j++];
        }
    }

    return m+n;
}

int main(int ac, char *av[])
{
    int a[] = {21, 28, 35, 40, 61, 75};
    int b[] = {8, 25, 26, 88};
    int c[20] = {0};
    int h = 0;
    int n = 0;
    
    n = merge(a, 6, b, 4, c);
    for (h=0; h<n; ++h) {
        printf("%d ", c[h]);
    }

    printf("\n");
    return 0;
}
