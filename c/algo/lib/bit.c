#include <bit.h>
#include <string.h>

int bit_get(const unsigned char *bits, int pos)
{
    unsigned char mask = 0x80;
    for (int i=0; i<(pos%8); i++) {
        mask = mask >> 1;
    }
    
    return (((mask & bits[(int)(pos / 8)]) == mask) ? 1 : 0);
}

void bit_set(unsigned char *bits, int pos, int state)
{
    unsigned char  mask = 0x80;
    for (int i=0; i<(pos%8); i++) {
       mask = mask >> 1;
    }
    
    if (0 != state) {
        bits[pos / 8] = bits[pos / 8] | mask;
    } else {
        bits[pos / 8] = bits[pos / 8] & (~mask);
    }
}

void bit_xor(const unsigned char *bits1, const unsigned char *bits2,
    unsigned char *bitsx, int size)
{
    for (int i=0; i<size; i++) {
       if (bit_get(bits1, i) != bit_get(bits2, i)) {
           bit_set(bitsx, i, 1);
       } else {
          bit_set(bitsx, i, 0);
       }
    }
}

void bit_rot_left(unsigned char *bits, int size, int count)
{
    
    int fbit = 0;
    int lbit = 0;
    int i = 0;
    int j = 0;
    
    if (size <= 0) {
        return;
    }
    
    for (j=0; j<count; j++) {
        for (i=0; i<=((size-1)/8); i++) {
            lbit = bit_get(&bits[i], 0);
            if (0  == i) {
                fbit = lbit;
            } else {
                bit_set(&bits[i - 1], 7, lbit);
            }
            
            bits[i] = bits[i] << 1;
        }
        
        bit_set(bits, size - 1, fbit);
    }
}
