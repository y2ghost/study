#include <geometry.h>
#include <math.h>

void arclen(spoint_t p1, spoint_t p2, double *length)
{
    point_t p1_rct;
    point_t p2_rct;
    double dot = 0;
    double alpha = 0;
    
    p1_rct.x = p1.rho * sin(p1.phi) * cos(p1.theta);
    p1_rct.y = p1.rho * sin(p1.phi) * sin(p1.theta);
    p1_rct.z = p1.rho * cos(p1.phi);
    p2_rct.x = p2.rho * sin(p2.phi) * cos(p2.theta);
    p2_rct.y = p2.rho * sin(p2.phi) * sin(p2.theta);
    p2_rct.z = p2.rho * cos(p2.phi);
    
    dot = (p1_rct.x * p2_rct.x) + (p1_rct.y * p2_rct.y) + (p1_rct.z * p2_rct.z);
    alpha = acos(dot / pow(p1.rho, 2.0));
    *length = alpha * p1.rho;
}
