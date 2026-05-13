#include <facttail.h>

int facttail(int n, int a)
{
    int rc = 0;
    if (n < 0) {
        return 0;
    } else if (0 == n) {
        rc = 1;
    } else if (1 == n) {
        rc = a;
    } else {
        rc = facttail(n-1, n*a);
    }

    return rc;
}
