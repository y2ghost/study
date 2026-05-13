#include <stdio.h>
#include <string.h>

#define MAX_NAME_SIZE   14
#define MAX_NAME_BUFF   MAX_NAME_SIZE+1
#define MAX_NAMES       8

void parallel_sort(int lo, int hi, int max, char list[][max], int id[])
{
    char key[max];
    int h = 0;

    for (h=lo+1; h<=hi; ++h) {
        strcpy(key, list[h]);
        int m = id[h];
        int k = h - 1;

        while (k>=lo && strcmp(key, list[k])<0) {
            strcpy(list[k+1], list[k]);
            id[k+1] = id[k];
            --k;
        }

        strcpy(list[k+1], key);
        id[k+1] = m;
    }
}

int main(int ac, char *av[])
{
    char name[MAX_NAMES][MAX_NAME_BUFF] = {
        "Taylor, Victor",
        "Duncan, Denise",
        "Ramdhan, Kamal",
        "Singh, Krishna",
        "Ali, Michael",
        "Sawh, Anisa",
        "Khan, Carol",
        "Owen, David"
    };
    int id[MAX_NAMES] = {3050, 2795, 4455, 7824, 6669, 5000, 5464, 6050};

    parallel_sort(0, MAX_NAMES-1, MAX_NAME_BUFF, name, id);
    printf("\nThe sorted names and IDs are\n\n");

    int h = 0;
    for (h=0; h<MAX_NAMES; ++h) {
        printf("%-18s %d\n", name[h], id[h]);
    }

    return 0;
}
