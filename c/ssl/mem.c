#include <string.h>
#include <openssl/bio.h>
#include <openssl/crypto.h>

void mem_test1(void)
{
    int i = 0;
    char *p = NULL;

    p = OPENSSL_malloc(4);
    p = OPENSSL_remalloc(p, 40);
    p = OPENSSL_realloc(p, 32);

    for (i=0; i<32; ++i) {
        memset(p+i, i, 1);
    }

    p = OPENSSL_realloc_clean(p, 32, 77);
    p = OPENSSL_remalloc(p, 40);
    OPENSSL_free(p);
    p = NULL;
}

void mem_test2(void)
{
    char *p = NULL;
    BIO *b = NULL;
    
    CRYPTO_malloc_debug_init();
    CRYPTO_set_mem_debug_options(V_CRYPTO_MDEBUG_ALL);
    CRYPTO_mem_ctrl(CRYPTO_MEM_CHECK_ON);
    p=OPENSSL_malloc(4);
    CRYPTO_mem_ctrl(CRYPTO_MEM_CHECK_OFF);
    b=BIO_new_file("leak.log","w");
    CRYPTO_mem_ctrl(CRYPTO_MEM_CHECK_ON);
    CRYPTO_mem_leaks(b);
    OPENSSL_free(p);
    p = NULL;
    BIO_free(b);
    b = NULL;
}

int main(int ac, char *av[])
{
    mem_test1();
    mem_test2();
    return 0;
}
