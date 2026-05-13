#include <facttail.h>
#include <stdio.h>

int main(int argc, char **argv)
{
    int n = 0;
    
    for (n=0; n<=13; n++) {
        fprintf(stdout, "tail recursive: %-10d\n", facttail(n, 1));
    }
    
    return 0;
}
