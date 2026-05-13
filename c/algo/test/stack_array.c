#include <stdio.h>
#include <stdlib.h>

#define ROGUE_VALUE -9999
#define MAX_STACK   10

typedef struct {
    int top;
    int st[MAX_STACK];
} ystack_t;

static ystack_t *_init_stack() {
    ystack_t *sp = (ystack_t*)malloc(sizeof(*sp));
    sp->top = -1;
    return sp;
}

static int _empty(ystack_t *s)
{
    return -1 == s->top;
}

static void _push(ystack_t *s, int n)
{
    if (s->top == MAX_STACK - 1) {
        printf("\nStack Overflow\n");
        exit(1);
    }

    ++(s->top);
    s->st[s->top] = n;
}

static int _pop(ystack_t *s)
{
    if (0 != _empty(s)) {
        return ROGUE_VALUE;
    }

    int hold = s->st[s->top];
    --(s->top);
    return hold;
}

int main(int ac, char *av[])
{
    int n = 0;
    ystack_t *s = NULL;
    
    s = _init_stack();
     printf("Enter some integers, ending with 0\n");
     scanf("%d", &n);

     while (0 != n) {
         _push(s, n);
         scanf("%d", &n);
     }

      printf("Numbers in reverse order\n");
      while (0 == _empty(s)) {
          printf("%d ", _pop(s));
      }

      printf("\n");
      return 0;
}
