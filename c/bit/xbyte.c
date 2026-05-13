typedef unsigned packed_t;

int xbyte_bad(packed_t word, int bytenum)
{
    int left = word << ((3 - bytenum) << 3);

    return left >> 24;
}
