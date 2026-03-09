#ifndef _YY_CSV_H_
#define _YY_CSV_H_

#include<stdio.h>

char *csvgetline(FILE *fp);
char *csvfield(int n);
int csvnfield(void);

#endif
