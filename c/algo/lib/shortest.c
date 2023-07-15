#include <graph.h>
#include <graphalg.h>
#include <list.h>
#include <set.h>
#include <float.h>
#include <stdlib.h>

static void relax(path_vertex_t *u, path_vertex_t *v, double weight)
{
    if (v->d > u->d + weight) {
        v->d = u->d + weight;
        v->parent = u;
    }
}

int shortest(graph_t *graph, const path_vertex_t *start, list_t *paths,
    int (*match) (const void *key1, const void *key2))
{
    adj_list_t *adjlist = NULL;
    path_vertex_t *pth_vertex = NULL;
    path_vertex_t *adj_vertex = NULL;
    list_elm_t *element = NULL;
    list_elm_t *member = NULL;
    double minimum = 0.0;
    int found = 0;
    int i = 0;
    
    found = 0;
    for (element=list_head(&graph_adjlists(graph)); NULL!=element;
        element=list_next(element)) {
        pth_vertex = ((adj_list_t *)list_data(element))->vertex;
        if (match(pth_vertex, start)) {
            pth_vertex->color = WHITE;
            pth_vertex->d = 0;
            pth_vertex->parent = NULL;
            found = 1;
        } else {
            pth_vertex->color = WHITE;
            pth_vertex->d = DBL_MAX;
            pth_vertex->parent = NULL;
        }
    }
    
    if (0 == found) {
        return -1;
    }
    
    i = 0;
    while (i < graph_vcount(graph)) {
        minimum = DBL_MAX;
        for (element=list_head(&graph_adjlists(graph)); NULL!=element;
            element=list_next(element)) {
            pth_vertex = ((adj_list_t *)list_data(element))->vertex;
            if (WHITE==pth_vertex->color && pth_vertex->d <minimum) {
                minimum = pth_vertex->d;
                adjlist = list_data(element);
            }
        }
    
        ((path_vertex_t *)adjlist->vertex)->color = BLACK;
        for (member=list_head(&adjlist->adjacent); NULL!=member;
            member=list_next(member)) {
            adj_vertex = list_data(member);
            for (element=list_head(&graph_adjlists(graph)); NULL!=element;
                element = list_next(element)) {
                pth_vertex = ((adj_list_t *)list_data(element))->vertex;
                if (match(pth_vertex, adj_vertex)) {
                    relax(adjlist->vertex, pth_vertex, adj_vertex->weight);
                }
            }
        }
    
        i++;
    }
    
    list_init(paths, NULL);
    for (element=list_head(&graph_adjlists(graph)); NULL!=element;
        element=list_next(element)) {
        pth_vertex = ((adj_list_t *)list_data(element))->vertex;
        if (BLACK == pth_vertex->color) {
            if (0 != list_ins_next(paths, list_tail(paths), pth_vertex)) {
                list_destroy(paths);
                return -1;
            }
        }
    }
    
    return 0;
}
