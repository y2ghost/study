#ifndef GRAPHALG_H
#define GRAPHALG_H

#include <graph.h>
#include <list.h>

typedef struct mst_vertex_t {
    void *data;
    double weight;
    vertex_color_t color;
    double key;
    struct mst_vertex_t *parent;
} mst_vertex_t;

typedef struct path_vertex_t {
    void *data;
    double weight;
    vertex_color_t color;
    double d;
    struct path_vertex_t *parent;
} path_vertex_t;

typedef struct tsp_vertex_t {
    void *data;
    double x;
    double y;
    vertex_color_t color;
} tsp_vertex_t;

int mst(graph_t *graph, const mst_vertex_t *start, list_t *span,
    int (*match)(const void *key1, const void *key2));
int shortest(graph_t *graph, const path_vertex_t *start, list_t *paths,
    int (*match) (const void *key1, const void *key2));
int tsp(list_t *vertices, const tsp_vertex_t *start, list_t *tour,
    int (*match) (const void *key1, const void *key2));

#endif /* GRAPHALG_H */
