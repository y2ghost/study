#include "array_list.h"
#include <assert.h>
#include <string.h>

array_list_t *add_to_array_list(array_list_t *array, void *data)
{
    assert(array->capacity > array->index);
    array->buf[array->index++] = data;
    return array;
}

void *remove_from_array_list(array_list_t *array, void *data)
{
    int i = 0;
    void **buf = NULL;
    void *tmp_data = NULL;

    for (i=0; i<array->index; ++i) {
        buf = array->buf + i;
        if (*buf == data) {
            memmove(buf, buf+1, (array->index-i-1) * sizeof(void *));
            --array->index;
            tmp_data = *buf;
            break;
        }
    }

    return tmp_data;
}

void *get_from_array_list(array_list_t *array, int index)
{
    assert(0 <= index && array->index > index);
    return array->buf[index];
}

size_t array_list_size(array_list_t *array)
{
    return array->index;
}
