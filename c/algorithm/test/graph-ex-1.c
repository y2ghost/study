#include <graph.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define STRSIZ  2

static void print_graph(const graph_t *graph)
{
    set_t *adjacent = NULL;
    list_elm_t *element = NULL;
    list_elm_t *member = NULL;
    int i = 0;
    int j = 0;
    
    fprintf(stdout, "Vertices=%d, edges=%d\n",
        graph_vcount(graph), graph_ecount(graph));
    i = 0;
    element = list_head(&graph_adjlists(graph));
    
    while (i < list_size(&graph_adjlists(graph))) {
        fprintf(stdout, "graph[%03d]=%s: ", i, (char *)((adj_list_t *)list_data(
            element))->vertex);
        j = 0;
        adjacent = &((adj_list_t *)list_data(element))->adjacent;
        member = list_head(adjacent);
    
        while (j < set_size(adjacent)) {
            if (j > 0) {
                fprintf(stdout, ", ");
            }

            fprintf(stdout, "%s", (char *)list_data(member));
            member = list_next(member);
            j++;
        }
    
        i++;
        fprintf(stdout, "\n");
        element = list_next(element);
    }
}

static int match_str(const void *str1, const void *str2)
{
    return !strcmp((const char *)str1, (const char *)str2);
}

int main(int argc, char **argv)
{
    graph_t graph;
    adj_list_t *adjlist = NULL;
    list_elm_t *element = NULL;
    char *data = NULL;
    char data1[STRSIZ] = {'\0'};
    char *data2 = NULL;
    int retval = 0;
    int size = 0;
    int i = 0;
    
    graph_init(&graph, match_str, free);
    data = (char *)malloc(STRSIZ);
    if (NULL == data) {
        return 1;
    }
    
    strcpy(data, "a");
    fprintf(stdout, "Inserting vertex %s\n", data);
    
    if (0 != graph_ins_vertex(&graph, data)) {
        return 1;
    }
    
    data = (char *)malloc(STRSIZ);
    if (NULL == data) {
        return 1;
    }
    
    strcpy(data, "b");
    fprintf(stdout, "Inserting vertex %s\n", data);
    
    if (0 != graph_ins_vertex(&graph, data)) {
        return 1;
    }
    
    data = (char *)malloc(STRSIZ);
    if (NULL == data) {
        return 1;
    }
    
    strcpy(data, "c");
    fprintf(stdout, "Inserting vertex %s\n", data);
    
    if (0 != graph_ins_vertex(&graph, data)) {
        return 1;
    }
    
    data = (char *)malloc(STRSIZ);
    if (NULL == data) {
        return 1;
    }
    
    strcpy(data, "d");
    fprintf(stdout, "Inserting vertex %s\n", data);
    
    if (0 != graph_ins_vertex(&graph, data)) {
        return 1;
    }
    
    data = (char *)malloc(STRSIZ);
    if (NULL == data) {
        return 1;
    }
    
    strcpy(data, "e");
    fprintf(stdout, "Inserting vertex %s\n", data);
    
    if (0 != graph_ins_vertex(&graph, data)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "a");
    strcpy(data2, "b");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "a");
    strcpy(data2, "c");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2)  { 
        return 1;
    }

    strcpy(data1, "b");
    strcpy(data2, "c");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "b");
    strcpy(data2, "d");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);
    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "c");
    strcpy(data2, "b");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "c");
    strcpy(data2, "c");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data) {
        return 1;
    }
    
    strcpy(data1, "c");
    strcpy(data2, "d");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "d");
    strcpy(data2, "a");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "e");
    strcpy(data2, "c");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "e");
    strcpy(data2, "d");
    fprintf(stdout, "Inserting edge %s to %s\n", data1, data2);
    
    if (0 != graph_ins_edge(&graph, data1, data2)) {
        return 1;
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "a");
    strcpy(data2, "c");
    data = data2;
    fprintf(stdout, "Removing edge %s to %s\n", data1, data2);
    
    if (0 != graph_rem_edge(&graph, data1, (void **)&data)) { 
        return 1;
    }
    
    free(data);
    print_graph(&graph);
    strcpy(data1, "c");
    strcpy(data2, "c");
    data = data2;
    fprintf(stdout, "Removing edge %s to %s\n", data1, data2);
    
    if (0 != graph_rem_edge(&graph, data1, (void **)&data)) {
        return 1;
    }
    
    free(data);
    print_graph(&graph);
    strcpy(data1, "e");
    strcpy(data2, "c");
    data = data2;
    fprintf(stdout, "Removing edge %s to %s\n", data1, data2);
    
    if (0 != graph_rem_edge(&graph, data1, (void **)&data)) {
        return 1;
    }
    
    free(data);
    print_graph(&graph);
    strcpy(data1, "a");
    strcpy(data2, "b");
    data = data2;
    fprintf(stdout, "Removing edge %s to %s\n", data1, data2);
    
    if (0 != graph_rem_edge(&graph, data1, (void **)&data)) {
        return 1;
    }
    
    free(data);
    print_graph(&graph);
    strcpy(data1, "d");
    strcpy(data2, "a");
    data = data2;
    fprintf(stdout, "Removing edge %s to %s\n", data1, data2);
    
    if (0 != graph_rem_edge(&graph, data1, (void **)&data)) {
        return 1;
    }
    
    free(data);
    print_graph(&graph);
    free(data2);
    strcpy(data1, "a");
    data = data1;
    fprintf(stdout, "Removing vertex %s\n", data1);
    
    if (0 != graph_rem_vertex(&graph, (void **)&data)) {
        return 1;
    }
    
    free(data);
    print_graph(&graph);
    
    data2 = (char *)malloc(STRSIZ);
    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "f");
    strcpy(data2, "a");
    retval = graph_ins_edge(&graph, data1, data2);
    fprintf(stdout,"Inserting an invalid edge from %s to %s...Value=%d (-1=OK)\n",
        data1, data2, retval);
    
    if (0 != retval) {
        free(data2);
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "c");
    strcpy(data2, "b");
    retval = graph_ins_edge(&graph, data1, data2);
    fprintf(stdout,"Inserting an existing edge from %s to %s...Value=%d (1=OK)\n",
        data1, data2, retval);
    
    if (0 != retval) {
        free(data2);
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "f");
    strcpy(data2, "a");
    data = data2;
    retval = graph_rem_edge(&graph, data1, (void **)&data);
    fprintf(stdout, "Removing an invalid edge from %s to %s...Value=%d (-1=OK)\n",
        data1, data2, retval);
    
    if (0 == retval) {
        free(data);
    }
    
    free(data2);
    print_graph(&graph);
    
    data2 = (char *)malloc(STRSIZ);
    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "c");
    strcpy(data2, "e");
    data = data2;
    retval = graph_rem_edge(&graph, data1, (void **)&data);
    fprintf(stdout, "Removing an invalid edge from %s to %s...Value=%d (-1=OK)\n",
        data1, data2, retval);
    
    if (0 == retval) {
        free(data);
    }
    
    free(data2);
    print_graph(&graph);
    
    data2 = (char *)malloc(STRSIZ);
    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data2, "c");
    retval = graph_ins_vertex(&graph, data2);
    fprintf(stdout, "Inserting an existing vertex %s...Value=%d (1=OK)\n", data1,
        retval);
    
    if (0 != retval) {
        free(data2);
    }
    
    print_graph(&graph);
    data2 = (char *)malloc(STRSIZ);

    if (NULL == data2) {
        return 1;
    }
    
    strcpy(data1, "b");
    strcpy(data2, "d");
    retval = graph_is_adjacent(&graph, data1, data2);
    fprintf(stdout, "Testing graph_is_adjacent (%s, %s)...Value=%d (1=OK)\n",
        data1, data2, retval);
    strcpy(data1, "a");
    strcpy(data2, "e");
    retval = graph_is_adjacent(&graph, data1, data2);
    fprintf(stdout, "Testing graph_is_adjacent (%s, %s)...Value=%d (0=OK)\n",
        data1, data2, retval);
    strcpy(data1, "e");
    strcpy(data2, "d");
    retval = graph_is_adjacent(&graph, data1, data2);
    fprintf(stdout, "Testing graph_is_adjacent (%s, %s)...Value=%d (1=OK)\n",
        data1, data2, retval);
    strcpy(data1, "c");
    strcpy(data2, "a");
    retval = graph_is_adjacent(&graph, data1, data2);
    fprintf(stdout, "Testing graph_is_adjacent (%s, %s)...Value=%d (0=OK)\n",
        data1, data2, retval);
    free(data2);
    strcpy(data1, "c");
    
    if (0 != graph_adjlist(&graph, data1, &adjlist)) {
        return 1;
    }
    
    fprintf(stdout, "Vertices adjacent to %s: ", data1);
    i = 0;
    size = set_size(&adjlist->adjacent);
    element = list_head(&adjlist->adjacent);
    
    while (i < size) {
        i++;
        if (i > 1) {
            fprintf(stdout, ", ");
        }

        fprintf(stdout, "%s", (char *)list_data(element));
        element = list_next(element);
    }
    
    fprintf(stdout, "\n");
    fprintf(stdout, "Destroying the graph\n");
    graph_destroy(&graph);
    return 0;
}
