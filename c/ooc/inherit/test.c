#include "circle.h"
#include "point.h"
#include "new.h"

#include <stdio.h>

int main(int argc, char **argv)
{
    void *p = NULL;

    while (*++argv) {
        switch (**argv) {
        case 'c':
            p = new(Circle, 1, 2, 3);
            break;
        case 'p':
            p = new(Point, 1, 2);
            break;
        default:
            continue;
        }

        draw(p);
        move(p, 10, 20);
        draw(p);
        delete(p);
    }

    return 0;
}
