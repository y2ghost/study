#include "utf8.h"
#include <arpa/inet.h>

#define _NXT    0x80
#define _SEQ2   0xc0
#define _SEQ3   0xe0
#define _SEQ4   0xf0
#define _SEQ5   0xf8
#define _SEQ6   0xfc
#define _BOM    0xfeff

static int wchar_forbidden(wchar_t sym)
{
    /* surrogate pairs */
    if (sym>=0xd800 && sym<=0xdfff) {
        return -1;
    }

    return 0;
}

static int utf8_forbidden(u_char octet)
{
    switch (octet) {
    case 0xc0:
    case 0xc1:
    case 0xf5:
    case 0xff:
        return -1;
    }

    return 0;
}

/*
 * DESCRIPTION
 *  This function translates UTF-8 string into UCS-4 string (all symbols
 *  will be in local machine byte order).
 *
 *  It takes the following arguments:
 *  in  - input UTF-8 string. It can be null-terminated.
 *  insize  - size of input string in bytes.
 *  out - result buffer for UCS-4 string. If out is NULL,
 *      function returns size of result buffer.
 *  outsize - size of out buffer in wide characters.
 *
 * RETURN VALUES
 *  The function returns size of result buffer (in wide characters).
 *  Zero is returned in case of error.
 *
 * CAVEATS
 *  1. If UTF-8 string contains zero symbols, they will be translated
 *     as regular symbols.
 *  2. If UTF8_IGNORE_ERROR or UTF8_SKIP_BOM flag is set, sizes may vary
 *     when `out' is NULL and not NULL. It's because of special UTF-8
 *     sequences which may result in forbidden (by RFC3629) UNICODE
 *     characters.  So, the caller must check return value every time and
 *     not prepare buffer in advance (\0 terminate) but after calling this
 *     function.
 */
size_t utf8_to_wchar(const char *in, size_t insize, wchar_t *out,
    size_t outsize, int flags)
{
    size_t i = 0;
    size_t n = 0;
    size_t total = 0;
    size_t n_bits = 0;
    wchar_t val = 0;
    wchar_t high = 0;
    u_char *p = NULL;
    u_char *lim = NULL;
    wchar_t *wlim = NULL;

    if (NULL==in || 0==insize || (0==outsize && NULL!=out)) {
        return 0;
    }

    total = 0;
    p = (u_char *)in;
    lim = p + insize;
    wlim = out + outsize;

    for (; p<lim; p+=n) {
        val = *p;
        if (0!=utf8_forbidden(val) && 0==(flags & UTF8_IGNORE_ERROR)) {
            return 0;
        }

        /* Get number of bytes for one wide character */
        /* default: 1 byte. Used when skipping bytes. */
        n = 1;
        if (0 == (val & 0x80)) {
            high = val;
        } else if (_SEQ2 == (val & 0xe0)) {
            n = 2;
            high = (wchar_t)(val & 0x1f);
        } else if (_SEQ3 == (val & 0xf0)) {
            n = 3;
            high = (wchar_t)(val & 0x0f);
        } else if (_SEQ4 == (val & 0xf8)) {
            n = 4;
            high = (wchar_t)(val & 0x07);
        } else if (_SEQ5 == (val & 0xfc)) {
            n = 5;
            high = (wchar_t)(val & 0x03);
        } else if (_SEQ6 == (val & 0xfe)) {
            n = 6;
            high = (wchar_t)(val & 0x01);
        } else {
            if (0 == (flags & UTF8_IGNORE_ERROR)) {
                return 0;
            }

            continue;
        }

        /* does the sequence header tell us truth about length? */
        if (lim-p <= n-1) {
            if (0 == (flags & UTF8_IGNORE_ERROR)) {
                return 0;
            }

            n = 1;
            continue;
        }

        /*
         * Validate sequence.
         * All symbols must have higher bits set to 10xxxxxx
         */
        if (n > 1) {
            for (i=1; i<n; i++) {
                if (_NXT != (p[i] & 0xc0)) {
                    break;
                }
            }

            if (i != n) {
                if (0 == (flags & UTF8_IGNORE_ERROR)) {
                    return 0;
                }

                n = 1;
                continue;
            }
        }

        total++;
        if (NULL == out) {
            continue;
        }

        if (out >= wlim) {
            return 0;    /* no space left */
        }

        *out = 0;
        n_bits = 0;

        for (i=1; i<n; i++) {
            *out |= (wchar_t)(p[n - i] & 0x3f) << n_bits;
            /* 6 low bits in every byte */
            n_bits += 6;
        }

        *out |= high << n_bits;
        if (0 != wchar_forbidden(*out)) {
            if (0 == (flags & UTF8_IGNORE_ERROR)) {
                /* forbidden character */
                return 0;
            } else {
                total--;
                out--;
            }
        } else if (_BOM==*out && 0!=(flags & UTF8_SKIP_BOM)) {
            total--;
            out--;
        }

        out++;
    }

    return total;
}

/*
 * DESCRIPTION
 *  This function translates UCS-4 symbols (given in local machine
 *  byte order) into UTF-8 string.
 *
 *  It takes the following arguments:
 *  in  - input unicode string. It can be null-terminated.
 *  insize  - size of input string in wide characters.
 *  out - result buffer for utf8 string. If out is NULL,
 *      function returns size of result buffer.
 *  outsize - size of result buffer.
 *
 * RETURN VALUES
 *  The function returns size of result buffer (in bytes). Zero is returned
 *  in case of error.
 *
 * CAVEATS
 *  If UCS-4 string contains zero symbols, they will be translated
 *  as regular symbols.
 */
size_t wchar_to_utf8(const wchar_t *in, size_t insize, char *out,
    size_t outsize, int flags)
{
    size_t n = 0;
    size_t total = 0;
    wchar_t ch = 0;
    wchar_t val = 0;
    u_char *p = NULL;
    u_char *oc = NULL;
    u_char *lim = NULL;
    wchar_t *w = NULL;
    wchar_t *wlim = NULL;

    if (NULL==in || 0==insize || (0==outsize && NULL!=out)) {
        return 0;
    }

    w = (wchar_t *)in;
    wlim = w + insize;
    p = (u_char *)out;
    lim = p + outsize;
    total = 0;

    for (; w < wlim; w++) {
        val = *w;
        if (0 != wchar_forbidden(val)) {
            if (0 == (flags & UTF8_IGNORE_ERROR)) {
                return 0;
            } else {
                continue;
            }
        }

        if (_BOM==val && 0!=(flags & UTF8_SKIP_BOM)) {
            continue;
        }

        if (val < 0) {
            if (0 == (flags & UTF8_IGNORE_ERROR)) {
                return 0;
            }

            continue;
        } else if (val <= 0x0000007f) {
            n = 1;
        } else if (val <= 0x000007ff) {
            n = 2;
        } else if (val <= 0x0000ffff) {
            n = 3;
        } else if (val <= 0x001fffff) {
            n = 4;
        } else if (val <= 0x03ffffff) {
            n = 5;
        } else {
            /* if (*w <= 0x7fffffff) */
            n = 6;
        }

        total += n;
        if (NULL == out) {
            continue;
        }

        if (lim-p <= n-1) {
            /* no space left */
            return 0;
        }

        /* make it work under different endians */
        ch = htonl(val);
        oc = (u_char *)&ch;

        switch (n) {
        case 1:
            *p = oc[3];
            break;
        case 2:
            p[1] = _NXT | (oc[3] & 0x3f);
            p[0] = _SEQ2 | (oc[3] >> 6) | ((oc[2] & 0x07) << 2);
            break;
        case 3:
            p[2] = _NXT | (oc[3] & 0x3f);
            p[1] = _NXT | (oc[3] >> 6) | ((oc[2] & 0x0f) << 2);
            p[0] = _SEQ3 | ((oc[2] & 0xf0) >> 4);
            break;
        case 4:
            p[3] = _NXT | (oc[3] & 0x3f);
            p[2] = _NXT | (oc[3] >> 6) | ((oc[2] & 0x0f) << 2);
            p[1] = _NXT | ((oc[2] & 0xf0) >> 4) | ((oc[1] & 0x03) << 4);
            p[0] = _SEQ4 | ((oc[1] & 0x1f) >> 2);
            break;
        case 5:
            p[4] = _NXT | (oc[3] & 0x3f);
            p[3] = _NXT | (oc[3] >> 6) | ((oc[2] & 0x0f) << 2);
            p[2] = _NXT | ((oc[2] & 0xf0) >> 4) | ((oc[1] & 0x03) << 4);
            p[1] = _NXT | (oc[1] >> 2);
            p[0] = _SEQ5 | (oc[0] & 0x03);
            break;
        case 6:
            p[5] = _NXT | (oc[3] & 0x3f);
            p[4] = _NXT | (oc[3] >> 6) | ((oc[2] & 0x0f) << 2);
            p[3] = _NXT | (oc[2] >> 4) | ((oc[1] & 0x03) << 4);
            p[2] = _NXT | (oc[1] >> 2);
            p[1] = _NXT | (oc[0] & 0x3f);
            p[0] = _SEQ6 | ((oc[0] & 0x40) >> 6);
            break;
        default:
            break;
        }

        /*
         * NOTE: do not check here for forbidden UTF-8 characters.
         * They cannot appear here because we do proper convertion.
         */
        p += n;
    }

    return total;
}
