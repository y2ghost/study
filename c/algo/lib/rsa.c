#include <encrypt.h>

static huge_t modexp(huge_t a, huge_t b, huge_t n)
{
    huge_t y = 0;
    
    y = 1;
    while (0 != b) {
       if (b & 1) {
           y = (y * a) % n;
       }

       a = (a * a) % n;
       b = b >> 1;
    }
    
    return y;
}

void rsa_encipher(huge_t plaintext, huge_t *ciphertext, rsa_pubkey_t pubkey)
{
    *ciphertext = modexp(plaintext, pubkey.e, pubkey.n);
}

void rsa_decipher(huge_t ciphertext, huge_t *plaintext, rsa_prikey_t prikey)
{
    *plaintext = modexp(ciphertext, prikey.d, prikey.n);
}
