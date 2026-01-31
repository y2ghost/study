#ifndef PRINT_RUSAGE_H
#define PRINT_RUSAGE_H

#include <sys/resource.h>

void printRusage(const char *leader, const struct rusage *ru);

#endif
