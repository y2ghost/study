#ifndef ENCRYPT_H
#define ENCRYPT_H

typedef unsigned long huge_t;

typedef struct rsa_pubkey_t {
    huge_t e;
    huge_t n;
} rsa_pubkey_t;

typedef struct rsa_prikey_t {
    huge_t d;
    huge_t n;
} rsa_prikey_t;

void des_encipher(const unsigned char *plaintext, unsigned char *ciphertext,
    const unsigned char *key);
void des_decipher(const unsigned char *ciphertext, unsigned char *plaintext,
    const unsigned char *key);
void rsa_encipher(huge_t plaintext, huge_t *ciphertext, rsa_pubkey_t pubkey);
void rsa_decipher(huge_t ciphertext, huge_t *plaintext, rsa_prikey_t prikey);

#endif /* ENCRYPT_H */
