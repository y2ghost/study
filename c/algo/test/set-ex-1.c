#include <list.h>
#include <set.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

static void print_set(const set_t *set)
{
    list_elm_t *member = NULL;
    int *data = NULL;
    int size = 0;
    int i = 0;
    
    fprintf(stdout, "set_t size is %d\n", size = set_size(set));
    i = 0;
    member = list_head(set);
    
    while (i < size) {
        data = list_data(member);
        fprintf(stdout, "set[%03d]=%03d\n", i, *data);
        member = list_next(member);
        i++;
    }
}

static int match_int(const void *key1, const void *key2)
{
    return !memcmp(key1, key2, sizeof(int));
}

int main(int argc, char **argv)
{
    set_t set;
    set_t set1;
    set_t set2;
    int *data = NULL;
    int retval = 0;
    int i = 0;
    
    set_init(&set, match_int, free);
    fprintf(stdout, "Inserting 10 members\n");
    
    for (i=9; i>=0; i--) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }
    
        *data = i + 1;
        retval = set_insert(&set, data);

        if (retval < 0) {
            return 1;
        } else if (1 == retval) {
            free(data);
        }
    }
    
    print_set(&set);
    fprintf(stdout, "Inserting the same members again\n");
    
    for (i=9; i>=0; i--) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }
    
        *data = i + 1;
        retval = set_insert(&set, data);

        if (retval < 0) {
            return 1;
        } else if (1 == retval) {
            free(data);
        }
    }
    
    print_set(&set);
    fprintf(stdout, "Inserting 200 and 100\n");
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 200;
    retval = set_insert(&set, data);

    if (retval < 0) {
        return 1;
    } else if (1 == retval) {
        free(data);
    }
    
    data = (int *)malloc(sizeof(int));
    if (NULL == data) {
        return 1;
    }
    
    *data = 100;
    retval = set_insert(&set, data);

    if (retval < 0) {
        return 1;
    } else if (1 == retval) {
        free(data);
    }
    
    print_set(&set);
    fprintf(stdout, "Removing 100, 5, and 10\n");
    i = 100;
    data = &i;
    
    if (0 == set_remove(&set, (void **)&data)) {
        free(data);
    }
    
    i = 5;
    data = &i;
    
    if (0 == set_remove(&set, (void **)&data)) {
        free(data);
    }
    
    i = 10;
    data = &i;
    
    if (0 == set_remove(&set, (void **)&data)) {
        free(data);
    }
    
    print_set(&set);
    fprintf(stdout, "Removing three members\n");
    
    if (0 == list_rem_next(&set, NULL, (void **)&data)) {
        free(data);
    }
    
    if (0 == list_rem_next(&set, NULL, (void **)&data)) {
        free(data);
    }
    
    if (0 == list_rem_next(&set, NULL, (void **)&data)) {
        free(data);
    }
    
    print_set(&set);
    fprintf(stdout, "Removing all members\n");
    
    while (set_size(&set) > 0) {
        if (0 == list_rem_next(&set, NULL, (void **)&data)) {
            free(data);
        }
    }
    
    print_set(&set);
    set_init(&set1, match_int, free);
    set_init(&set2, match_int, free);
    
    for (i=9; i>=0; i--) {
        data = (int *)malloc(sizeof(int));
        if (NULL == data) {
            return 1;
        }
    
        *data = i + 1;
        retval = set_insert(&set1, data);

        if (retval < 0) {
            return 1;
        } else if (1 == retval) {
            free(data);
        }
    
        if (5==i || 6==i || 7==i) {
            data = (int *)malloc(sizeof(int));
            if (NULL == data) {
                return 1;
            }
    
            *data = i * 10;
            retval = set_insert(&set2, data);

            if (retval < 0) {
                return 1;
            } else if (1 == retval) {
                free(data);
            }
        } else if (3==i || 1==i) {
            data = (int *)malloc(sizeof(int));
            if (NULL == data) {
                return 1;
            }
    
            *data = i;
            retval = set_insert(&set2, data);

            if (retval < 0) {
                return 1;
            } else if (1 == retval) {
                free(data);
            }
        }
    }
    
    fprintf(stdout, "set_t 1 for testing unions, intersections, and differences\n");
    print_set(&set1);
    fprintf(stdout, "set_t 2 for testing unions, intersections, and differences\n");
    print_set(&set2);
    fprintf(stdout, "Determining the union of the two sets\n");
    
    if (0 != set_union(&set, &set1, &set2)) {
        return 1;
    }
    
    print_set(&set);
    fprintf(stdout, "Destroying the union\n");
    set_destroy(&set);
    fprintf(stdout, "Determining the intersection of the two sets\n");
    
    if (0 != set_intersection(&set, &set1, &set2)) {
        return 1;
    }
    
    print_set(&set);
    fprintf(stdout, "Testing whether the intersection equals set_t 1...Value=%d "
        "(0=OK)\n", set_is_equal(&set, &set1));
    fprintf(stdout, "Testing whether set_t 1 equals itself...Value=%d (1=OK)\n",
        set_is_equal(&set1, &set1));
    fprintf(stdout, "Testing whether the intersection is a subset of set_t 1..."
        "Value=%d (1=OK)\n", set_is_subset(&set, &set1));
    fprintf(stdout, "Testing whether set_t 2 is a subset of set_t 1...Value=%d "
        "(0=OK)\n", set_is_subset(&set2, &set1));
    fprintf(stdout, "Destroying the intersection\n");
    set_destroy(&set);
    fprintf(stdout, "Determining the difference of the two sets\n");
    
    if (0 != set_difference(&set, &set1, &set2)) {
        return 1;
    }
    
    print_set(&set);
    i = 3;
    fprintf(stdout, "Testing whether %03d is in difference...Value=%d (0=OK)\n",i,
        set_is_member(&set, &i));
    i = 5;
    fprintf(stdout, "Testing whether %03d is in difference...Value=%d (1=OK)\n",i,
        set_is_member(&set, &i));
    fprintf(stdout, "Destroying the sets\n");
    set_destroy(&set);
    set_destroy(&set1);
    set_destroy(&set2);
    return 0;
}
