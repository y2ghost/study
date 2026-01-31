#ifndef GRAPH_H
#define GRAPH_H

#include "set.h"
#include "list.h"

typedef struct adj_list_t {
    void *vertex;
    set_t adjacent;
} adj_list_t;

typedef struct graph_t {
    int vcount;
    int ecount;
    int (*match)(const void *key1, const void *key2);
    void (*destroy)(void *data);
    list_t adjlists;
} graph_t;

typedef enum vertex_color_t {
    WHITE,
    GRAY,
    BLACK,
} vertex_color_t;

void graph_init(graph_t *graph,
    int (*match)(const void *key1, const void *key2),
    void (*destroy)(void *data));
void graph_destroy(graph_t *graph);
int graph_ins_vertex(graph_t *graph, const void *data);
int graph_ins_edge(graph_t *graph, const void *data1, const void *data2);
int graph_rem_vertex(graph_t *graph, void **data);
int graph_rem_edge(graph_t *graph, void *data1, void **data2);
int graph_adjlist(const graph_t *graph, const void *data, adj_list_t **adjlist);
int graph_is_adjacent(const graph_t *graph,
    const void *data1, const void *data2);

#define graph_adjlists(graph)   ((graph)->adjlists)
#define graph_vcount(graph)     ((graph)->vcount)
#define graph_ecount(graph)     ((graph)->ecount)

#endif /* GRAPH_H */
