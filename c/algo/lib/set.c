#include <set.h>
#include <stdlib.h>
#include <string.h>

void set_init(set_t *set,
    int (*match)(const void *key1, const void *key2),
    void (*destroy)(void *data))
{
    list_init(set, destroy);
    set->match = match;
}

int set_insert(set_t *set, const void *data)
{
    if (set_is_member(set, data)) {
        return 1;
    }

    return list_ins_next(set, list_tail(set), data);
}

int set_remove(set_t *set, void **data)
{
    list_elm_t *member = NULL;
    list_elm_t *prev = NULL;
    
    for (member=list_head(set); NULL!=member; member=list_next(member)) {
        if (set->match(*data, list_data(member))) {
            break;
        }
    
        prev = member;
    }
    
    if (NULL == member) {
        return -1;
    }
    
    return list_rem_next(set, prev, data);
}

int set_union(set_t *setu, const set_t *set1, const set_t *set2)
{
    list_elm_t *member = NULL;
    void *data = NULL;
    
    set_init(setu, set1->match, NULL);
    for (member=list_head(set1); NULL!=member; member=list_next(member)) {
        data = list_data(member);
        if (0 != list_ins_next(setu, list_tail(setu), data)) {
            set_destroy(setu);
            return -1;
        }
    }

    for (member=list_head(set2); NULL!=member; member=list_next(member)) {
        if (set_is_member(set1, list_data(member))) {
            continue;
        }

        data = list_data(member);
        if (0 != list_ins_next(setu, list_tail(setu), data)) {
            set_destroy(setu);
            return -1;
        }
    }
    
    return 0;
}

int set_intersection(set_t *seti, const set_t *set1, const set_t *set2)
{
    list_elm_t *member = NULL;
    void *data = NULL;
    
    set_init(seti, set1->match, NULL);
    for (member=list_head(set1); NULL!=member; member=list_next(member)) {
        if (set_is_member(set2, list_data(member))) {
            data = list_data(member);
            if (0 != list_ins_next(seti, list_tail(seti), data)) {
                set_destroy(seti);
                return -1;
            }
        }
    }
    
    return 0;
}

int set_difference(set_t *setd, const set_t *set1, const set_t *set2)
{
    list_elm_t *member = NULL;
    void *data = NULL;
    
    set_init(setd, set1->match, NULL);
    for (member=list_head(set1); NULL!=member; member=list_next(member)) {
        if (!set_is_member(set2, list_data(member))) {
            data = list_data(member);
            if (0 != list_ins_next(setd, list_tail(setd), data)) {
                set_destroy(setd);
                return -1;
            }
        }
    }
    
    return 0;
}

int set_is_member(const set_t *set, const void *data)
{
    list_elm_t *member = NULL;
    
    for (member=list_head(set); NULL!=member; member=list_next(member)) {
        if (set->match(data, list_data(member))) {
            return 1;
        }
    }
    
    return 0;
}

int set_is_subset(const set_t *set1, const set_t *set2)
{
    list_elm_t *member = NULL;
    
    if (set_size(set1) > set_size(set2)) {
        return 0;
    }
    
    for (member=list_head(set1); NULL!=member; member=list_next(member)) {
        if (!set_is_member(set2, list_data(member))) {
            return 0;
        }
    }
    
    return 1;
}

int set_is_equal(const set_t *set1, const set_t *set2)
{
    if (set_size(set1) != set_size(set2)) {
        return 0;
    }
    
    return set_is_subset(set1, set2);
}
