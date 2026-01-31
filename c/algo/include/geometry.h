#ifndef GEOMETRY_H
#define GEOMETRY_H

#include <list.h>

#ifndef PI
#define PI  3.14159
#endif

#define MIN(x, y)       (((x) < (y)) ? (x) : (y))
#define MAX(x, y)       (((x) > (y)) ? (x) : (y))
#define DEGTORAD(deg)   (((deg) * 2.0 * PI) / 360.0)
#define RADTODEG(rad)   (((rad) * 360.0) / (2.0 * PI))

typedef struct point_t {
    double x;
    double y;
    double z;
} point_t;

typedef struct spoint_t {
    double rho;
    double theta;
    double phi;
} spoint_t;

int lint(point_t p1, point_t p2, point_t p3, point_t p4);
int cvxhull(const list_t *P, list_t *polygon);
void arclen(spoint_t p1, spoint_t p2, double *length);

#endif /* GEOMETRY_H */
