#ifndef _INT_SORTER_H_
#define _INT_SORTER_H_

#include "buffer.h"
#include "file_accessor.h"

#ifdef __cplusplus
extern "C" {
#endif

enum sort_error_e {
    ERR_CAT_OK,
    ERR_CAT_FILE,
    ERR_CAT_MEMORY,
};

enum sort_error_e int_sorter(const char *fname);

#ifdef __cplusplus
}
#endif

#endif /* _INT_SORTER_H_ */
