#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdio.h>

#define SPC_BASE16_TO_10(x) (((x) >= '0' && (x) <= '9') ? ((x) - '0') : \
    (toupper((x)) - 'A' + 10))

char *_decode_url(const char *url, size_t *nbytes)
{
    char *out = NULL;
    char *ptr = NULL;
    const char *c = NULL;

    ptr = strdup(url);
    if (NULL == ptr) {
        return NULL;
    }

    out = ptr;
    for (c=url; '\0'!=*c; c++) {
        if (*c != '%' || !isxdigit(c[1]) || !isxdigit(c[2])) {
            *ptr++ = *c;
        } else {
            *ptr++ = (SPC_BASE16_TO_10(c[1]) * 16) + (SPC_BASE16_TO_10(c[2]));
          c += 2;
        }
    }

    *ptr = '\0';
    if (NULL != nbytes) {
        *nbytes = (ptr - out); /* does not include null byte */
    }

    return out;
}

int main(int ac, char *av[])
{
    char url[] = "http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=monline_dg"
        "&wd=dsafsdakjlfsadfsad%20dasfkl%25%25%25%25%25%25&oq=dsafsdakjlfsad"
        "fsad&rsv_pq=e3994abe0003cdf8&rsv_t=0edc9kEnFUkFUhLzvMnRSdZ9G6Zuha9r"
        "E56ksfkOw8EhMBZDB1HLie%2FOl1r0v%2BBEYA&rsv_enter=1&rsv_sug3=14&rsv_"
        "sug2=0&inputT=2500&rsv_sug4=2570";
    size_t nbytes = 0;
    char *new_url = _decode_url(url, &nbytes);
    printf("the url is %s\n", new_url);
    return 0;
}
