#include <stdio.h>

void selection_sort(int list[], int lo, int hi)
{
    int get_smallest(int [], int, int);
    void swap(int [], int, int);

    int h = 0;
    for (h=lo; h<hi; ++h) {
        int s = get_smallest(list, h, hi);
        swap(list, h, s);
    }
}

int get_smallest(int list[], int lo, int hi)
{
    int small = lo;
    int h = 0;

    for (h=lo+1; h<=hi; ++h) {
        if (list[h] < list[small]) {
            small = h;
        }
    }

    return small;
}

void swap(int list[], int i, int j)
{
    int hold = list[i];
    list[i] = list[j];
    list[j] = hold;
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

    selection_sort(num, 0, n-1);
    printf("\nthe sorted numbers are\n");

    int h = 0;
    for (h=0; h<n; ++h) {
        printf("%d ", num[h]);
    }

    printf("\n");
    return 0;
}
