#include "csv.h"
#include <stdlib.h>
#include <string.h>

#define NUL '\0'
#define QUT '"'
#define SEM ','

static int split(void);
static void reset(void);
static char *advquoted(char *p);
static int endofline(FILE *fp, int c);

typedef struct {
    char *line;
    char *spit;
    char **field;
    char *fieldsep;
    int nfield;
    int maxline;
    int maxfield;
} st_csv;

static st_csv csv = { NULL, NULL, NULL, ",", 0, 0, 0 };

char *csvgetline(FILE * fp)
{
    int i = 0;
    int c = 0;
    char *newl = NULL;
    char *news = NULL;

    if (NULL == csv.line) {
        csv.maxline = 1;
        csv.maxfield = 1;
        csv.line = (char*)malloc(csv.maxline);
        csv.spit = (char*)malloc(csv.maxline);
        csv.field = (char**)malloc(csv.maxfield * sizeof(csv.field[0]));

        if (NULL==csv.line || NULL==csv.spit || NULL==csv.field) {
            reset();
            return NULL;
        }
    }

    for (i=0; EOF!=(c=getc(fp)) && !endofline(fp,c); i++) {
        if (i >= csv.maxline-1) {
            csv.maxline *= 2;
            newl = (char*)realloc(csv.line, csv.maxline);
            news = (char*)realloc(csv.spit, csv.maxline);

            if (NULL==newl || NULL==news) {
                reset();
                return NULL;
            }

            csv.line = newl;
            csv.spit = news;
        }

        csv.line[i] = c;
    }

    csv.line[i] = NUL;
    if (-1 == split()) {
        reset();
        return NULL;
    }

    return (EOF==c && 0==i) ? NULL:csv.line;
}

static void reset(void)
{
    free(csv.line);
    free(csv.spit);
    free(csv.field);
    csv.line = NULL;
    csv.spit = NULL;
    csv.field = NULL;
    csv.maxline = 0;
    csv.maxfield = 0;
    csv.nfield = 0;
}

static int endofline(FILE * fp, int c)
{
    int eol = 0;

    if ('\r'==c || '\n'==c) {
        eol = 1;
    }

    if ('\r' == c) {
        c = getc(fp);
        if ('\n' != c && EOF != c) {
            ungetc(c, fp);
        }
    }

    return eol;
}

static int split(void)
{
    char *p = NULL;
    char **newf = NULL;
    char *sepp = NULL;
    int sepc = 0;

    csv.nfield = 0;
    if (NUL == csv.line[0]) {
        return 0;
    }

    strcpy(csv.spit, csv.line);
    p = csv.spit;

    do {
        if (csv.nfield >= csv.maxfield) {
            csv.maxfield *= 2;
            newf = (char**) realloc(csv.field, csv.maxfield * sizeof(csv.field[0]));

            if (NULL == newf) {
                return -1;
            }

            csv.field = newf;
        }

        if (QUT == *p) {
            sepp = advquoted(++p);
        } else {
            sepp = p + strcspn(p, csv.fieldsep);
        }

        sepc = sepp[0];
        sepp[0] = NUL;
        csv.field[csv.nfield++] = p;
        p = sepp + 1;
    } while (SEM == sepc);

    return csv.nfield;
}

static char *advquoted(char *p)
{
    int i = 0;
    int j = 0;

    for (i=0, j=0; NUL!=p[j]; i++, j++) {
        if (QUT==p[j] && QUT!=p[++j]) {
            int k = strcspn(p+j, csv.fieldsep);
            memmove(p+i, p+j, k);
            i += k;
            j += k;
            break;
        }

        p[i] = p[j];
    }

    p[i] = NUL;
    return p + j;
}

char *csvfield(int n)
{
    if (n<0 || n>=csv.nfield) {
        return NULL;
    }

    return csv.field[n];

}

int csvnfield(void)
{
    return csv.nfield;
}
