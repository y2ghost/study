#include <openssl/pem.h>
#include <openssl/ssl.h>
#include <openssl/rsa.h>
#include <openssl/err.h>
#include <stdio.h>

/*
 * private key: openssl genrsa -out private.pem 2048
 * public key: openssl rsa -in private.pem -outform PEM -pubout -out public.pem
 */

static int padding = RSA_PKCS1_PADDING;

RSA *createRSAFromFile(const char *fname, int public)
{
    FILE *fp = fopen(fname, "rb");
    if (NULL == fp) {
        printf("Unable to open file %s\n", fname);
        return NULL;
    }

    RSA *rsa= RSA_new();
    if (0 != public) {
        rsa = PEM_read_RSA_PUBKEY(fp, &rsa, NULL, NULL);
    } else {
        rsa = PEM_read_RSAPrivateKey(fp, &rsa, NULL, NULL);
    }

    fclose(fp);
    return rsa;
}

int main(int ac, char *av[])
{
    char text[] = "hwver";
    unsigned char encrypted[4098] = {0};
    unsigned char decrypted[4098] = {0};
    RSA *rsa = createRSAFromFile("./public.pem", 1);
    int size = RSA_size(rsa);
    size_t text_len = strlen(text);
    printf("the rsa size: %d\n", size);
    printf("the text length: %d\n", text_len);

    int enc_len = RSA_public_encrypt(2, text,
        encrypted, rsa, padding);
    printf("Encrypted length =%d\n", enc_len);
    RSA_free(rsa);

    rsa = createRSAFromFile("./private.pem", 0);
    int dec_len = RSA_private_decrypt(enc_len, encrypted,
        decrypted, rsa, padding);
    unsigned long err = ERR_get_error();
    char err_buf[512] = {0};
    ERR_error_string_n(err, err_buf, 512);
    printf("Decrypted Text =%s\n", decrypted);
    printf("Decrypted Length =%d\n", dec_len);
    printf("err: %s\n", err_buf);
    RSA_free(rsa);
    return 0;
}
