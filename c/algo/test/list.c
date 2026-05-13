#include <stdio.h>
#include <stdlib.h>

typedef struct node {
    int num;
    struct node *next;
} node;

static node *_make_node(int n)
{
    node *np = (node*)malloc(sizeof(*np));
    np->num = n;
    np->next = NULL;
    return np;
}

static void _print_list(node *np)
{
    while (NULL != np) {
        printf("%d\n", np->num);
        np = np->next;
    }
}

static void _free_list(node **top)
{
    if (NULL != top) {
        node *next = *top;
        while (NULL != next) {
            node *cur = next;
            next = next->next;
            free(cur);
        }

        *top = NULL;
    }
}

node *_add_inplace(node *top, int n)
{
    node *np = NULL;
    node *cur = NULL;
    node *prev = NULL;

    np = _make_node(n);
    cur = top;

    while (NULL!=cur && n>cur->num) {
        prev = cur;
        cur = cur->next;
    }

    if (NULL == prev) {
        np->next = top;
        return np;
    }

    np->next = cur;
    prev->next = np;
    return top;
}

int main(int ac, char *av[])
{
    int n = 0;
    node *top = NULL;

    if (1 != scanf("%d", &n)) {
        n = 0;
    }

    while (0 != n) {
        top = _add_inplace(top, n);
        if (1 != scanf("%d", &n)) {
            n = 0;
        }
    }

    _print_list(top);
    _free_list(&top);
    return 0;
}
