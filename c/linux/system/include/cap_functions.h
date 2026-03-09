#ifndef CAP_FUNCTIONS_H
#define CAP_FUNCTIONS_H

#include <stdio.h>
#include <sys/capability.h>
#include <stdbool.h>

int modifyCapSetting(cap_flag_t flag, int capability, int setting);
void printSecbits(int secbits, bool verbose, FILE *fp);

#endif

