#include <compress.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>

#define DATSIZ  500000

int main(int argc, char **argv)
{
    FILE *fp = NULL;
    unsigned char original[DATSIZ] = {0};
    unsigned char *compressed = NULL;
    unsigned char *restored = NULL;
    int csize = 0;
    int osize = 0;
    int rsize = 0;
    int c = 0; int i = 0;
    
    fp = fopen("sample.txt", "r");
    if (NULL == fp) {
        return 1;
    }
    
    i = 0;
    
    while (1) {
        c = getc(fp);
        if (EOF==c || i>=DATSIZ) {
            break;
        }

        original[i] = c;
        i++;
    }
    
    osize = i;
    fclose(fp);
    fp = NULL;
    fprintf(stdout, "Compressing with Huffman coding\n");
    fprintf(stdout, "Compressing...");
    
    csize = huffman_compress(original, &compressed, osize);
    if (csize < 0) {
        fprintf(stdout, "\n");
        return 1;
    }
    
    fprintf(stdout, "Done\n");
    fprintf(stdout, "Uncompressing...");
    
    rsize = huffman_uncompress(compressed, &restored);
    if (rsize < 0) {
        fprintf(stdout, "\n");
        free(compressed);
        compressed = NULL;
        return 1;
    }
    
    fprintf(stdout, "Done\n");
    fprintf(stdout, "osize=%d, csize=%d, rsize=%d\n", osize, csize, rsize);
    
    if (rsize != osize) {
        fprintf(stdout, "Data was not properly restored\n");
        free(compressed);
        compressed = NULL;
        free(restored);
        restored = NULL;
        return 1;
    } else {
        for (i=0; i<rsize; i++) {
            if (original[i] != restored[i]) {
                fprintf(stdout, "Data was not properly restored\n");
                if (isgraph(original[i])) {
                    fprintf(stdout, "original[%d]=\"%c\"\n", i, original[i]);
                } else {
                    fprintf(stdout, "original[%d]=0x%02x\n", i, original[i]);
                }
    
                if (isgraph(restored[i])) {
                    fprintf(stdout, "restored[%d]=\"%c\"\n", i, restored[i]);
                } else {
                    fprintf(stdout, "restored[%d]=0x%02x\n", i, restored[i]);
                }
    
                free(compressed);
                compressed = NULL;
                free(restored);
                restored = NULL;
                return 1;
            }
        }
    }
    
    fprintf(stdout, "Data was restored OK\n");
    free(compressed);
    compressed = NULL;
    free(restored);
    restored = NULL;
    fprintf(stdout, "Compressing with LZ77\n");
    fprintf(stdout, "Compressing...");
    
    csize = lz77_compress(original, &compressed, osize);
    if (csize < 0) {
        return 1;
    }
    
    fprintf(stdout, "Done\n");
    fprintf(stdout, "Uncompressing...");
    
    rsize = lz77_uncompress(compressed, &restored);
    if (rsize < 0) {
        fprintf(stdout, "\n");
        free(compressed);
        compressed = NULL;
        return 1;
    }
    
    fprintf(stdout, "Done\n");
    fprintf(stdout, "osize=%d, csize=%d, rsize=%d\n", osize, csize, rsize);
    
    if (rsize != osize) {
        fprintf(stdout, "Data was not properly restored\n");
        free(compressed);
        compressed = NULL;
        free(restored);
        restored = NULL;
        return 1;
    } else {
        for (i=0; i<rsize; i++) {
            if (original[i] != restored[i]) {
                fprintf(stdout, "Data was not properly restored\n");
                if (isgraph(original[i])) {
                    fprintf(stdout, "original[%d]=\"%c\"\n", i, original[i]);
                } else {
                    fprintf(stdout, "original[%d]=0x%02x\n", i, original[i]);
                }
    
                if (isgraph(restored[i])) {
                    fprintf(stdout, "restored[%d]=\"%c\"\n", i, restored[i]);
                } else {
                    fprintf(stdout, "restored[%d]=0x%02x\n", i, restored[i]);
                }
    
                free(compressed);
                compressed = NULL;
                free(restored);
                restored = NULL;
                return 1;
            }
        }
    }
    
    fprintf(stdout, "Data was restored OK\n");
    free(compressed);
    compressed = NULL;
    free(restored);
    restored = NULL;
    return 0;
}
