#include <geometry.h>
#include <list.h>
#include <math.h>
#include <stdlib.h>

int cvxhull(const list_t *P, list_t *polygon)
{
    list_elm_t *element = NULL;
    point_t *min = NULL;
    point_t *low = NULL;
    point_t *p0 = NULL;
    point_t *pi = NULL;
    point_t *pc = NULL;
    double z = 0;
    double length1 = 0;
    double length2 = 0;
    int count = 0;
    
    min = list_data(list_head(P));
    for (element=list_head(P); NULL!=element;
        element=list_next(element)) {
        p0 = list_data(element);

        if (p0->y < min->y) {
            min = p0;
            low = list_data(element);
        } else {
            if (p0->y==min->y && p0->x<min->x) {
                min = p0;
                low = list_data(element);
            }
        }
    }
    
    list_init(polygon, NULL);
    p0 = low;
    
    do {
        if (0 != list_ins_next(polygon, list_tail(polygon), p0)) {
            list_destroy(polygon);
            return -1;
        }

        count = 0;
        for (element=list_head(P); NULL!=element;
            element=list_next(element)) {
            pi = list_data(element);
            if (pi == p0) {
                continue;
            }
    
            count++;
            if (1 == count) {
                pc = list_data(element);
                continue;
            }
    
            z = ((pi->x-p0->x) * (pc->y-p0->y)) - ((pi->y-p0->y) * (pc->x-p0->x));
            if (z > 0) {
                pc = pi;
            } else if (0 == z) {
                length1 = sqrt(pow(pi->x - p0->x, 2.0) + pow(pi->y - p0->y, 2.0));
                length2 = sqrt(pow(pc->x - p0->x, 2.0) + pow(pc->y - p0->y, 2.0));
                
                if (length1 > length2) {
                    pc = pi;
                }
            }
        }
    
        p0 = pc;
    } while (p0 != low);
    
    return 0;
}
