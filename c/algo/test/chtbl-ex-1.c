#include <chtbl.h>
#include <stdio.h>
#include <stdlib.h>

#define TBLSIZ  11

static int match_char(const void *char1, const void *char2)
{
    return (*(const char *)char1 == *(const char *)char2);
}

static int h_char(const void *key)
{
    return *(const char *)key % TBLSIZ;
}

static void print_table(const chtbl_t *htbl)
{
    list_elm_t *element = NULL;
    int i = 0;
    
    fprintf(stdout, "Table size is %d\n", chtbl_size(htbl));
    for (i=0; i<TBLSIZ; i++) {
        fprintf(stdout, "Bucket[%03d]=", i);
        for (element=list_head(&htbl->table[i]); NULL!=element;
            element=list_next(element)) {
            fprintf(stdout, "%c", *(char *)list_data(element));
        }
        
        fprintf(stdout, "\n");
    }
}

int main(int argc, char **argv)
{
    chtbl_t htbl;
    char *data = NULL;
    char c = '\0';
    int retval = 0;
    int i = 0;
    
    if (0 != chtbl_init(&htbl, TBLSIZ, h_char, match_char, free)) {
        return 1;
    }
    
    for (i=0; i<TBLSIZ; i++) {
        data = (char *)malloc(sizeof(char));
        if (NULL == data) {
            return 1;
        }
    
        *data = ((5 + (i * 6)) % 23) + 'A';
        if (0 != chtbl_insert(&htbl, data)) {
            return 1;
        }
    
        print_table(&htbl);
    }
    
    for (i=0; i<TBLSIZ; i++) {
        data = (char *)malloc(sizeof(char));
        if (NULL == data) {
            return 1;
        }
    
        *data = ((3 + (i * 4)) % 23) + 'a';
        if (0 != chtbl_insert(&htbl, data)) {
            return 1;
        }
    
        print_table(&htbl);
    }
    
    data = (char *)malloc(sizeof(char));
    if (NULL == data) {
        return 1;
    }
    
    *data = 'd';
    retval = chtbl_insert(&htbl, data);

    if (0 != retval) {
        free(data);
    }
    
    fprintf(stdout, "Trying to insert d again...Value=%d (1=OK)\n", retval);
    data = (char *)malloc(sizeof(char));

    if (NULL == data) {
        return 1;
    }

    *data = 'G';
    retval = chtbl_insert(&htbl, data);
    if (0 != retval) {
        free(data);
    }
    
    fprintf(stdout, "Trying to insert G again...Value=%d (1=OK)\n", retval);
    fprintf(stdout, "Removing d, G, and S\n");
    c = 'd';
    data = &c;
    
    if (0 == chtbl_remove(&htbl, (void **)&data)) {
        free(data);
    }
    
    c = 'G';
    data = &c;
    
    if (0 == chtbl_remove(&htbl, (void **)&data)) {
        free(data);
    }
    
    c = 'S';
    data = &c;
    
    if (0 == chtbl_remove(&htbl, (void **)&data)) {
        free(data);
    }
    
    print_table(&htbl);
    data = (char *)malloc(sizeof(char));
    if (NULL == data) {
        return 1;
    }
    
    *data = 'd';
    retval = chtbl_insert(&htbl, data);
    if (0 != retval) {
        free(data);
    }
    
    fprintf(stdout, "Trying to insert d again...Value=%d (0=OK)\n", retval);
    data = (char *)malloc(sizeof(char));

    if (NULL == data) {
        return 1;
    }
    
    *data = 'G';
    retval = chtbl_insert(&htbl, data);
    if (0 != retval) {
        free(data);
    }
    fprintf(stdout, "Trying to insert G again...Value=%d (0=OK)\n", retval);
    print_table(&htbl);
    fprintf(stdout, "Inserting X and Y\n");
    
    data = (char *)malloc(sizeof(char));
    if (NULL == data) {
        return 1;
    }
    
    *data = 'X';
    if (0 != chtbl_insert(&htbl, data)) {
        return 1;
    }
    
    data = (char *)malloc(sizeof(char));
    if (NULL == data) {
        return 1;
    }
    
    *data = 'Y';
    if (0 != chtbl_insert(&htbl, data)) {
        return 1;
    }
    
    print_table(&htbl);
    c = 'X';
    data = &c;
    
    if (0 == chtbl_lookup(&htbl, (void **)&data)) {
        fprintf(stdout, "Found an occurrence of X\n");
    } else {
        fprintf(stdout, "Did not find an occurrence of X\n");
    }
    
    c = 'Z';
    data = &c;
    
    if (0 == chtbl_lookup(&htbl, (void **)&data)) {
        fprintf(stdout, "Found an occurrence of Z\n");
    } else {
        fprintf(stdout, "Did not find an occurrence of Z\n");
    }
    
    fprintf(stdout, "Destroying the hash table\n");
    chtbl_destroy(&htbl);
    return 0;
}
