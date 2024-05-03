#include <stdio.h>
#include <string.h>

#define MAX_NAME_SIZE   14
#define MAX_NAME_BUFF   MAX_NAME_SIZE+1
#define MAX_NAMES       8

int binary_search(int lo, int hi, char key[], int max, char list[][max])
{
    while (lo <= hi) {
        int mid = (lo + hi) / 2;
        int cmp = strcmp(key, list[mid]);

        if (0 == cmp) {
            return mid;
        }

        if (cmp < 0) {
            hi = mid - 1;
        } else {
            lo = mid + 1;
        }
    }

    return -1;
}

int main (int ac, char *av[])
{
    int n = 0;
    char name[MAX_NAMES][MAX_NAME_BUFF] = {
        "Ali, Michael",
        "Duncan, Denise",
        "Khan, Carol",
        "Owen, David",
        "Ramdhan, Kamal",
        "Sawh, Anisa",
        "Singh, Krishna",
        "Taylor, Victor"
    };
    
    n = binary_search(0, 7, "Ali, Michael", MAX_NAME_BUFF, name);
    printf("%d\n", n);
    
    n = binary_search(0, 7, "Taylor, Victor", MAX_NAME_BUFF, name);
    printf("%d\n", n);
    
    n = binary_search(0, 7, "Owen, David", MAX_NAME_BUFF, name);
    printf("%d\n", n);
    
    n = binary_search(4, 7, "Owen, David", MAX_NAME_BUFF, name);
    printf("%d\n", n);
    
    n = binary_search(0, 7, "Sandy, Cindy", MAX_NAME_BUFF, name);
    printf("%d\n", n);
    return 0;
}
