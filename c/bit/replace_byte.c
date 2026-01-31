unsigned replace_byte(unsigned x, int i, unsigned char b)
{
    int shift = i << 3;
    unsigned mask = 0xff << shift;

    return (x & ~mask) | (b << shift);
}
