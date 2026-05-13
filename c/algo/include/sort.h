#ifndef SORT_H
#define SORT_H

typedef int (*compare)(const void *key1, const void *key2);

int issort(void *data, int size, int esize, compare cmp);
int qksort(void *data, int size, int esize, int i, int k, compare cmp);
int mgsort(void *data, int size, int esize, int i, int k, compare cmp);
int ctsort(int *data, int size, int k);
int rxsort(int *data, int size, int p, int k);

#endif /* SORT_H */
