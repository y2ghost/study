#include <stdio.h>
#include <string.h>
#include <openssl/bio.h>
#include <openssl/rand.h>

int main(int ac, char *av[])
{
    char buf[20] = {'\0'};
    const char *p = NULL;
    unsigned char out[20] = {'\0'};
    char filename[50] = {'\0'};
    int ret = 0;
    BIO *print = NULL;
    
    strcpy(buf, "my random number");
    RAND_add(buf, 20, strlen(buf));
    strcpy(buf, "23424d");
    RAND_seed(buf, 20);
    
    while(1) {
        ret = RAND_status();
        if (1 == ret) {
            printf("seeded enough!\n");
            break;
        } else {
            RAND_poll();
        }
    }
    
    p = RAND_file_name(filename, 50);
    if (NULL == p) {
        printf("can not get rand file\n");
        return -1;
    }
    
    ret = RAND_write_file(p);
    RAND_load_file(p, 1024);
    ret = RAND_bytes(out, 20);
    
    if (1 != ret) {
        printf("err.\n");
        return -1;
    }

    print = BIO_new(BIO_s_file());
    BIO_set_fp(print, stdout, BIO_NOCLOSE);
    BIO_write(print, out, 20);
    BIO_write(print, "\n", 2);
    BIO_free(print);
    RAND_cleanup();
    return 0;
}
