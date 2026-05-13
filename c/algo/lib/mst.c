#include <graph.h>
#include <graphalg.h>
#include <list.h>
#include <float.h>
#include <stdlib.h>

int mst(graph_t *graph, const mst_vertex_t *start, list_t *span, int (*match)(const
    void *key1, const void *key2))
{
    adj_list_t *adjlist = NULL;
    mst_vertex_t *mst_vertex = NULL;
    mst_vertex_t *adj_vertex = NULL;
    list_elm_t *element = NULL;
    list_elm_t *member = NULL;
    double minimum = 0;
    int found = 0;
    int i = 0;
    
    found = 0;
    for (element=list_head(&graph_adjlists(graph));
        NULL!=element; element=list_next(element)) {
        mst_vertex = ((adj_list_t *)list_data(element))->vertex;
        if (match(mst_vertex, start)) {
            mst_vertex->color = WHITE;
            mst_vertex->key = 0;
            mst_vertex->parent = NULL;
            found = 1;
        } else {
            mst_vertex->color = WHITE;
            mst_vertex->key = DBL_MAX;
            mst_vertex->parent = NULL;
        }
    }
    
    if (0 == found) {
        return -1;
    }
    
    i = 0;
    while (i < graph_vcount(graph)) {
        minimum = DBL_MAX;
        for (element=list_head(&graph_adjlists(graph));
            NULL!=element; element=list_next(element)) {
            mst_vertex = ((adj_list_t *)list_data(element))->vertex;
            if (WHITE==mst_vertex->color && mst_vertex->key<minimum) {
                minimum = mst_vertex->key;
                adjlist = list_data(element);
            }
        }
    
        ((mst_vertex_t *)adjlist->vertex)->color = BLACK;
        for (member=list_head(&adjlist->adjacent);
            NULL!=member; member=list_next(member)) {
            adj_vertex = list_data(member);
            for (element=list_head(&graph_adjlists(graph));
                NULL!=element; element=list_next(element)) {
                mst_vertex = ((adj_list_t *)list_data(element))->vertex;
                if (match(mst_vertex, adj_vertex)) {
                    if (WHITE==mst_vertex->color &&
                        adj_vertex->weight<mst_vertex->key) {
                        mst_vertex->key = adj_vertex->weight;
                        mst_vertex->parent = adjlist->vertex;
                    }
    
                break;
                }
            }
        }
    
        i++;
    }
    
    list_init(span, NULL);
    for (element=list_head(&graph_adjlists(graph));
        NULL!=element; element=list_next(element)) {
        mst_vertex = ((adj_list_t *)list_data(element))->vertex;
        if (BLACK == mst_vertex->color) {
            if (0 != list_ins_next(span, list_tail(span), mst_vertex)) {
                list_destroy(span);
                return -1;
            }
        }
    }
    
    return 0;
}
