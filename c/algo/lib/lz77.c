#include <bit.h>
#include <compress.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>

static int compare_win(const unsigned char *window,
    const unsigned char *buffer, int *offset, unsigned char *next)
{
    int match = 0;
    int longest = 0;
    int i = 0;
    int j = 0;
    int k = 0;
    
    *offset = 0;
    longest = 0;
    *next = buffer[0];
    
    for (k=0; k<LZ77_WINDOW_SIZE; k++) {
        i = k;
        j = 0;
        match = 0;

        while (i<LZ77_WINDOW_SIZE && j<LZ77_BUFFER_SIZE-1) {
            if (window[i] != buffer[j]) {
                break;
            }
    
            match++;
            i++;
            j++;
        }
    
        if (match > longest) {
            *offset = k;
            longest = match;
            *next = buffer[j];
        }
    }
    
    return longest;
}

int lz77_compress(const unsigned char *original, unsigned char **compressed,
    int size)
{
    unsigned long token = 0;
    unsigned char window[LZ77_WINDOW_SIZE] = {0};
    unsigned char buffer[LZ77_BUFFER_SIZE] = {0};
    unsigned char *comp = NULL;
    unsigned char *temp = NULL;
    unsigned char next = 0;
    int offset = 0;
    int length = 0;
    int remaining = 0;
    int tbits = 0;
    int hsize = 0;
    int ipos = 0;
    int opos = 0;
    int tpos = 0;
    int i = 0;
    
    *compressed = NULL;
    hsize = sizeof(int);
    
    comp = (unsigned char *)malloc(hsize);
    if (NULL == comp) {
        return -1;
    }
    
    memcpy(comp, &size, sizeof(int));
    memset(window, 0, LZ77_WINDOW_SIZE);
    memset(buffer, 0, LZ77_BUFFER_SIZE);
    ipos = 0;
    
    for (i=0; i<LZ77_BUFFER_SIZE && ipos<size; i++) {
        buffer[i] = original[ipos];
        ipos++;
    }
    
    opos = hsize * 8;
    remaining = size;
    
    while (remaining > 0) {
        length = compare_win(window, buffer, &offset, &next);
        if (0 != length) {
            token = 0x00000001 << (LZ77_PHRASE_BITS - 1);
            token = token | (offset << (LZ77_PHRASE_BITS - LZ77_TYPE_BITS -
                LZ77_WINOFF_BITS));
            token = token | (length << (LZ77_PHRASE_BITS - LZ77_TYPE_BITS -
                LZ77_WINOFF_BITS - LZ77_BUFLEN_BITS));
            token = token | next;
            tbits = LZ77_PHRASE_BITS;
        } else {
            token = 0x00000000;
            token = token | next;
            tbits = LZ77_SYMBOL_BITS;
        }
        
        token = htonl(token);
        for (i=0; i<tbits; i++) {
            if (opos % 8 == 0) {
                temp = (unsigned char *)realloc(comp,(opos / 8) + 1);
                if (NULL == temp) {
                    free(comp);
                    return -1;
                }
                
                comp = temp;
            }
    
            tpos = (sizeof(unsigned long) * 8) - tbits + i;
            bit_set(comp, opos, bit_get((unsigned char *)&token, tpos));
            opos++;
        }
    
        length++;
        memmove(&window[0], &window[length], LZ77_WINDOW_SIZE - length);
        memmove(&window[LZ77_WINDOW_SIZE - length], &buffer[0], length);
        memmove(&buffer[0], &buffer[length], LZ77_BUFFER_SIZE - length);
        
        for (i=LZ77_BUFFER_SIZE-length; i<LZ77_BUFFER_SIZE && ipos<size; i++) {
            buffer[i] = original[ipos];
            ipos++;
        }
        
        remaining = remaining - length;
    }

    *compressed = comp;
    return ((opos - 1) / 8) + 1;
}

int lz77_uncompress(const unsigned char *compressed,
    unsigned char **original)
{
    unsigned char window[LZ77_WINDOW_SIZE] = {0};
    unsigned char buffer[LZ77_BUFFER_SIZE] = {0};
    unsigned char *orig = NULL;
    unsigned char *temp = NULL;
    unsigned char next = 0;
    int offset = 0;
    int length = 0;
    int remaining = 0;
    int hsize = 0;
    int size = 0;
    int ipos = 0;
    int opos = 0;
    int tpos = 0;
    int state = 0;
    int i = 0;
    

    if (remaining <= 0) {
        return 0;
    }
    
    *original = NULL;
    orig = NULL;
    hsize = sizeof(int);
    memcpy(&size, compressed, sizeof(int));
    memset(window, 0, LZ77_WINDOW_SIZE);
    memset(buffer, 0, LZ77_BUFFER_SIZE);
    ipos = hsize * 8;
    opos = 0;
    remaining = size;
    state = bit_get(compressed, ipos);
    ipos++;
    
    if (1 == state) {
        memset(&offset, 0, sizeof(int));
        for (i=0; i<LZ77_WINOFF_BITS; i++) {
            tpos = (sizeof(int) * 8) - LZ77_WINOFF_BITS + i;
            bit_set((unsigned char *)&offset, tpos, bit_get(compressed, ipos));
            ipos++;
        }

        memset(&length, 0, sizeof(int));
        for (i=0; i<LZ77_BUFLEN_BITS; i++) {
            tpos = (sizeof(int) * 8) - LZ77_BUFLEN_BITS + i;
            bit_set((unsigned char *)&length, tpos, bit_get(compressed, ipos));
            ipos++;
        }
        
        next = 0x00;
        for (i=0; i<LZ77_NEXT_BITS; i++) {
            tpos = (sizeof(unsigned char) * 8) - LZ77_NEXT_BITS + i;
            bit_set((unsigned char *)&next, tpos, bit_get(compressed, ipos));
            ipos++;
        }
        
        offset = ntohl(offset);
        length = ntohl(length);
        i = 0;
        
        if (opos > 0) {
            temp = (unsigned char *)realloc(orig, opos+length+1);
            if (NULL == temp) {
                free(orig);
                return -1;
            }
        
            orig = temp;
        } else {
            orig = (unsigned char *)malloc(length + 1);
            if (NULL == orig) {
                return -1;
            }
        
            while (i < length && remaining > 0) {
                orig[opos] = window[offset + i];
                opos++;
                buffer[i] = window[offset + i];
                i++;
                remaining--;
            }
        
            if (remaining > 0) {
                orig[opos] = next;
                opos++;
                buffer[i] = next;
                remaining--;
            }
        
            length++;
        }
    } else {
        next = 0x00;
        for (i=0; i<LZ77_NEXT_BITS; i++) {
            tpos = (sizeof(unsigned char) * 8) - LZ77_NEXT_BITS + i;
            bit_set((unsigned char *)&next, tpos, bit_get(compressed, ipos));
            ipos++;
        }
    
        if (opos > 0) {
            temp = (unsigned char *)realloc(orig, opos + 1);
            if (NULL == temp) {
                free(orig);
                return -1;
            }
    
            orig = temp;
        } else {
            orig = (unsigned char *)malloc(1);
            if (NULL == orig) {
                return -1;
            }
        }
    
        orig[opos] = next;
        opos++;
        
        if (remaining > 0) {
            buffer[0] = next;
        }
    
        remaining--;
        length = 1;
    }
    
    memmove(&window[0], &window[length], LZ77_WINDOW_SIZE - length);
    memmove(&window[LZ77_WINDOW_SIZE - length], &buffer[0], length);
    *original = orig;
    return opos;
}
