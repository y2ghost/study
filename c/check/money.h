#ifndef MONEY_H
#define MONEY_H

typedef struct money money;

money *money_create(int amount, char *currency);
int money_amount(money *m);
char *money_currency(money *m);
void money_free(money *m);

#endif /* MONEY_H */
