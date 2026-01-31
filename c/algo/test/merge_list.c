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

static node *_make_list()
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

    return top;
}

node *_merge_list(node *a, node *b)
{
    node *c = NULL;
    node *last = NULL;

    if (NULL == a) {
        return b;
    }

    if (NULL == b) {
        return a;
    }

    while (NULL!=a && NULL!=b) {
        if (a->num < b->num) {
            if (NULL == c) {
                c = a;
            } else {
                last->next = a;
            }

            last = a;
            a = a->next;
        } else {
            if (NULL == c) {
                c = b;
            } else {
                last->next = b;
            }

            last = b;
            b = b->next;
        }
    }

    if (NULL == a) {
        last->next = b;
    } else {
        last->next = a;
    }

    return c;
}

int main(int ac, char *av[])
{
    node *a = NULL;
    node *b = NULL;
    node *c = NULL;

    printf("Enter numbers for the first list (0 to end)\n");
    a = _make_list();

    printf("Enter numbers for the second list (0 to end)\n");
    b = _make_list();

    c = _merge_list(a, b);
    printf("\nThe merged list is\n");
    _print_list(c);
    _free_list(&c);
    return 0;
}
