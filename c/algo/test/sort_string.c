#include <stdio.h>
#include <string.h>

#define MAX_NAME_SIZE     14
#define MAX_NAME_BUFF   MAX_NAME_SIZE+1
#define MAX_NAMES        8

void insert_sort(int lo, int hi, int max, char list[][max])
{
    char key[max];
    int h = 0;

    for (h=lo+1; h<=hi; ++h) {
        strcpy(key, list[h]);
        int k = h - 1;

        while (k>=lo && strcmp(key, list[k])<0) {
            strcpy(list[k+1], list[k]);
            --k;
        }

        strcpy(list[k+1], key);
    }
}

int main(int ac, char *av[]) {
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
   
    insert_sort(0, MAX_NAMES-1, MAX_NAME_BUFF, name);
    printf("\nThe sorted names are\n\n");
    
    int h = 0;
    for (h = 0; h < MAX_NAMES; h++) {
        printf("%s\n", name[h]);
    }

    return 0;
}
