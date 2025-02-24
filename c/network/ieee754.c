#include <stdio.h>
#include <stdint.h>
#include <inttypes.h>

#define pack754_32(f) (pack754((f), 32, 8))
#define pack754_64(f) (pack754((f), 64, 11))
#define unpack754_32(i) (unpack754((i), 32, 8))
#define unpack754_64(i) (unpack754((i), 64, 11))

uint64_t pack754(long double f, unsigned bits, unsigned expbits)
{
    if (f == 0.0) {
        return 0;
    }

    long long sign = 0;
    long double fnorm = 0.0;

    if (f < 0) {
        sign = 1;
        fnorm = -f;
    } else {
        sign = 0;
        fnorm = f;
    }

    int shift = 0;
    while(fnorm >= 2.0) { fnorm /= 2.0; shift++; }
    while(fnorm < 1.0) { fnorm *= 2.0; shift--; }
    fnorm = fnorm - 1.0;
    unsigned significandbits = bits - expbits - 1;
    long long significand = fnorm * ((1LL<<significandbits) + 0.5f);
    long long exp = shift + ((1<<(expbits-1)) - 1);
    return (sign<<(bits-1)) | (exp<<(bits-expbits-1)) | significand;
}

long double unpack754(uint64_t i, unsigned bits, unsigned expbits)
{
    if (i == 0) {
        return 0.0;
    }

    unsigned significandbits = bits - expbits - 1;
    long double result = (i&((1LL<<significandbits)-1));
    result /= (1LL<<significandbits);
    result += 1.0f;

    unsigned bias = (1<<(expbits-1)) - 1;
    long long shift = ((i>>significandbits)&((1LL<<expbits)-1)) - bias;
    while(shift > 0) { result *= 2.0; shift--; }
    while(shift < 0) { result /= 2.0; shift++; }
    result *= (i>>(bits-1))&1? -1.0: 1.0;
    return result;
}

int main(void)
{
    float f = 3.1415926;
    uint32_t fi = pack754_32(f);
    float f2 = unpack754_32(fi);

    double d = 3.14159265358979323;
    uint64_t di = pack754_64(d);
    double d2 = unpack754_64(di);

    printf("float before : %.7f\n", f);
    printf("float encoded: 0x%08" PRIx32 "\n", fi);
    printf("float after  : %.7f\n\n", f2);
    printf("double before : %.20lf\n", d);
    printf("double encoded: 0x%016" PRIx64 "\n", di);
    printf("double after  : %.20lf\n", d2);
    return 0;
}

