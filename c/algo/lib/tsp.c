#include <graph.h>
#include <graphalg.h>
#include <list.h>
#include <float.h>
#include <math.h>
#include <stdlib.h>

int tsp(list_t *vertices, const tsp_vertex_t *start, list_t *tour,
    int (*match) (const void *key1, const void *key2))
{
    tsp_vertex_t *tsp_vertex = NULL;
    tsp_vertex_t *tsp_start = NULL;
    tsp_vertex_t *selection = NULL;
    list_elm_t *element = NULL;
    double minimum = 0;
    double distance = 0;
    double x = 0;
    double y = 0;
    int found = 0;
    int i = 0;
    
    list_init(tour, NULL);
    found = 0;
    
    for (element=list_head(vertices); NULL!=element;
        element=list_next(element)) {
        tsp_vertex = list_data(element);

        if (match(tsp_vertex, start)) {
            if (0 != list_ins_next(tour, list_tail(tour), tsp_vertex)) {
                list_destroy(tour);
                return -1;
            }
    
            tsp_start = tsp_vertex;
            x = tsp_vertex->x;
            y = tsp_vertex->y;
            tsp_vertex->color = BLACK;
            found = 1;
        } else {
            tsp_vertex->color = WHITE;
        }
    }
    
    if (0 == found) {
        list_destroy(tour);
        return -1;
    }
    
    i = 0;
    while (i < list_size(vertices) - 1) {
        minimum = DBL_MAX;
        for (element=list_head(vertices); NULL!=element;
            element=list_next(element)) {
            tsp_vertex = list_data(element);
            if (WHITE == tsp_vertex->color) {
                distance = sqrt(pow(tsp_vertex->x-x,2.0) + pow(tsp_vertex->y-y,2.0));
                if (distance < minimum) {
                    minimum = distance;
                    selection = tsp_vertex;
                }
            }
        }
    
        x = selection->x;
        y = selection->y;
        selection->color = BLACK;

        if (0 != list_ins_next(tour, list_tail(tour), selection)) {
            list_destroy(tour);
            return -1;
        }
    
        i++;
    }
    
    if (0 != list_ins_next(tour, list_tail(tour), tsp_start)) {
        list_destroy(tour);
        return -1;
    }
    
    return 0;
}
