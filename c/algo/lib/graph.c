#include <graph.h>
#include <stdlib.h>
#include <string.h>

void graph_init(graph_t *graph,
    int (*match)(const void *key1, const void *key2),
    void (*destroy)(void *data))
{
    graph->vcount = 0;
    graph->ecount = 0;
    graph->match = match;
    graph->destroy = destroy;
    list_init(&graph->adjlists, NULL);
}

void graph_destroy(graph_t *graph)
{
    adj_list_t *adjlist = NULL;
    while (list_size(&graph->adjlists) > 0) {
        if (0 == list_rem_next(&graph->adjlists, NULL, (void **)&adjlist)) {
            set_destroy(&adjlist->adjacent);
            if (NULL != graph->destroy) {
                graph->destroy(adjlist->vertex);
            }
            
            free(adjlist);
        }
    }
    
    list_destroy(&graph->adjlists);
    memset(graph, 0, sizeof(graph_t));
}

int graph_ins_vertex(graph_t *graph, const void *data)
{
    list_elm_t *element = NULL;
    for (element=list_head(&graph->adjlists);
        NULL!=element; element=list_next(element)) {
        if (graph->match(data, ((adj_list_t *)list_data(element))->vertex)) {
            return 1;
        }
    }
    
    adj_list_t *adjlist = (adj_list_t *)malloc(sizeof(adj_list_t));
    if (NULL == adjlist) {
        return -1;
    }
    
    adjlist->vertex = (void *)data;
    set_init(&adjlist->adjacent, graph->match, graph->destroy);
    
    int retval = list_ins_next(&graph->adjlists,
        list_tail(&graph->adjlists), adjlist);
    if (0 != retval) {
        return retval;
    }
    
    graph->vcount++;
    return 0;
}

int graph_ins_edge(graph_t *graph, const void *data1, const void *data2)
{
    list_elm_t *element = NULL;
    for (element=list_head(&graph->adjlists);
        NULL!=element; element=list_next(element)) {
        if (graph->match(data2, ((adj_list_t *)list_data(element))->vertex)) {
            break;
        }
    }
    
    if (NULL == element) {
        return -1;
    }
    
    for (element=list_head(&graph->adjlists);
        NULL!=element; element=list_next(element)) {
        if (graph->match(data1, ((adj_list_t *)list_data(element))->vertex)) {
            break;
        }
    }
    
    if (NULL == element) {
        return -1;
    }
    
    int retval = set_insert(&((adj_list_t *)list_data(element))->adjacent, data2);
    if (0 != retval) {
       return retval;
    }
    
    graph->ecount++;
    return 0;
}

int graph_rem_vertex(graph_t *graph, void **data)
{
    
    list_elm_t *element = NULL;
    list_elm_t *temp = NULL;
    list_elm_t *prev = NULL;
    adj_list_t *adjlist = NULL;
    int found = 0;
    
    for (element=list_head(&graph->adjlists);
        NULL!=element; element=list_next(element)) {
        if (set_is_member(&((adj_list_t *)list_data(element))->adjacent, *data)) {
            return -1;
        }
    
        if (graph->match(*data, ((adj_list_t *)list_data(element))->vertex)) {
            temp = element;
            found = 1;
        }
    
        if (0 == found) {
            prev = element;
        }
    }
     
    if (0 == found) {
        return -1;
    }
    
    if (set_size(&((adj_list_t *)list_data(temp))->adjacent) > 0) {
        return -1;
    }
    
    if (list_rem_next(&graph->adjlists, prev, (void **)&adjlist) != 0) {
        return -1;
    }
    
    *data = adjlist->vertex;
    free(adjlist);
    graph->vcount--;
    return 0;
}

int graph_rem_edge(graph_t *graph, void *data1, void **data2)
{
    list_elm_t *element = NULL;
    for (element=list_head(&graph->adjlists);
        NULL!=element; element=list_next(element)) {
        if (graph->match(data1, ((adj_list_t *)list_data(element))->vertex)) {
            break;
        }
    }
    
    if (NULL == element) {
        return -1;
    }
    
    if (0 != set_remove(&((adj_list_t *)list_data(element))->adjacent, data2)) {
        return -1;
    }
    
    graph->ecount--;
    return 0;
}

int graph_adjlist(const graph_t *graph, const void *data, adj_list_t **adjlist)
{
    list_elm_t *element = NULL;
    for (element=list_head(&graph->adjlists);
        NULL!=element; element=list_next(element)) {
        if (graph->match(data, ((adj_list_t *)list_data(element))->vertex)) {
            break;
        }
    }
    
    if (NULL == element) {
        return -1;
    }
    
    *adjlist = list_data(element);
    return 0;
}

int graph_is_adjacent(const graph_t *graph, const void *data1, const void
    *data2)
{
    list_elm_t *element = NULL;
    for (element=list_head(&graph->adjlists);
        NULL!=element; element=list_next(element)) {
        if (graph->match(data1, ((adj_list_t *)list_data(element))->vertex)) {
            break;
        }
    }
    
    if (NULL == element) {
        return 0;
    }
    
    return set_is_member(&((adj_list_t *)list_data(element))->adjacent, data2);
}
