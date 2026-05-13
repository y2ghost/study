#ifndef _ARRAY_LIST_H_
#define _ARRAY_LIST_H_

#include <stdbool.h>
#include <stddef.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct array_list_t array_list_t;

struct array_list_t {
    const int capacity;
    void ** const buf;
    size_t index;
    struct array_list_t *(* const add)(struct array_list_t *self, void *data);
    void *(* const remove)(struct array_list_t *self, void *data);
    void *(* const get)(struct array_list_t *self, int index);
    size_t (* const size)(struct array_list_t *self);
};

array_list_t *add_to_array_list(array_list_t *array, void *data);
void *remove_from_array_list(array_list_t *array, void *data);
void *get_from_array_list(array_list_t *array, int index);
size_t array_list_size(array_list_t *array);

#define new_array_list(array) { \
    sizeof(array) / sizeof(void *), array, 0, add_to_array_list, \
    remove_from_array_list, get_from_array_list, array_list_size}

#ifdef __cplusplus
}
#endif

#endif /* _ARRAY_LIST_H_ */
