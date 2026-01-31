int is_little_endian(void)
{
    /* MSB = 0, LSB = 1 */
    int x = 1;

    return (int)(*(char *)&x);
}
