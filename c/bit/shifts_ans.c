int int_shifts_are_arith(void)
{
    int x = ~0;

    return (x >> 1) == x;
}
