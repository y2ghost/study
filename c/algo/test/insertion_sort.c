#include <stdio.h>

void insert_place(int item, int list[], int m, int n)
{
    int k = n;
    while (k>=m && item<list[k]) {
        list[k+1] = list[k];
        --k;
    }

    list[k+1] = item;
}

void insertion_sort2(int list[], int lo, int hi)
{
    int h = 0;
    for (h=lo+1; h<=hi; ++h) {
        insert_place(list[h], list, lo, h-1);
    }
}

void insertion_sort(int list[], int n)
{
    int h = 0;
    for (h=1; h<n; ++h) {
        int key = list[h];
        int k = h - 1;

        while (k>=0 && key<list[k]) {
            list[k+1] = list[k];
            --k;
        }

        list[k+1] = key;
    }
}

static const int MAX_NUMBERS = 10;

int main(void)
{
    int num[MAX_NUMBERS];

    printf("type up to %d numbers followed by 0\n", MAX_NUMBERS);
    int val = 0;
    int n = 0;

    while (n<MAX_NUMBERS) {
        scanf("%d", &val);
        if (0 == val) {
            break;
        }

        num[n++] = val;
    }

    if (0 != val) {
        printf("more than %d numbers entered\n", MAX_NUMBERS);
    }

    //insertion_sort(num, n);
    insertion_sort2(num, 0, n-1);
    printf("\nthe sorted numbers are\n");

    int h = 0;
    for (h=0; h<n; ++h) {
        printf("%d ", num[h]);
    }

    printf("\n");
    return 0;
}
