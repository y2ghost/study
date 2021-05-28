#include "money.h"
#include <stdlib.h>

struct money {
    int amount;
    char *currency;
};

money *money_create(int amount, char *currency)
{
    if (amount < 0) {
        return NULL;
    }

    money *m = malloc(sizeof(money));
    if (NULL == m) {
        return NULL;
    }

    m->amount = amount;
    m->currency = currency;
    return m;
}

int money_amount(money *m)
{
    return m->amount;
}

char *money_currency(money *m)
{
    return m->currency;
}

void money_free(money *m)
{
    free(m);
    return;
}
