#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/safestack.h>

#define sk_student_new(st)      SKM_sk_new(student, (st))
#define sk_student_new_null()   SKM_sk_new_null(student)
#define sk_student_free(st)     SKM_sk_free(student, (st))
#define sk_student_num(st)      SKM_sk_num(student, (st))
#define sk_student_value(st, i) SKM_sk_value(student, (st), (i))
#define sk_student_set(st, i, val)  SKM_sk_set(student, (st), (i), (val))
#define sk_student_zero(st)         SKM_sk_zero(student, (st))
#define sk_student_push(st, val)    SKM_sk_push(student, (st), (val))
#define sk_student_unshift(st, val) SKM_sk_unshift(student, (st), (val))
#define sk_student_find(st, val)    SKM_sk_find(student, (st), (val))
#define sk_student_delete(st, i)    SKM_sk_delete(student, (st), (i))
#define sk_student_delete_ptr(st, ptr) \
    SKM_sk_delete_ptr(student, (st), (ptr))
#define sk_student_insert(st, val, i) \
    SKM_sk_insert(student, (st), (val), (i))
#define sk_student_set_cmp_func(st, cmp) \
    SKM_sk_set_cmp_func(student, (st), (cmp))
#define sk_student_dup(st)  SKM_sk_dup(student, st)
#define sk_student_pop_free(st, free_func) \
    SKM_sk_pop_free(student, (st), (free_func))
#define sk_student_shift(st)    SKM_sk_shift(student, (st))
#define sk_student_pop(st)      SKM_sk_pop(student, (st))
#define sk_student_sort(st)     SKM_sk_sort(student, (st))

typedef struct student_st {
    char *name;
    int age;
    char *other_info;
} student;

typedef STACK_OF(student) students;

student *student_malloc()
{
    student *s = malloc(sizeof(*s));
    s->name = malloc(20);
    strcpy(s->name, "zcp");
    s->other_info = malloc(20);
    s->age = 100;
    strcpy(s->other_info, "no_info");
    return s;
}

void student_free(student *s)
{
    free(s->name);
    free(s->other_info);
    memset(s, 0x0, sizeof(*s));
    free(s);
}

static int student_cmp(student *a, student *b)
{
    return strcmp(a->name, b->name);
}

int main(int ac, char *av[])
{
    students *s = NULL;
    students *s_new = NULL;
    student *s1 = NULL;
    student *s2 = NULL;
    int i = 0;
    int num = 0;

    s = sk_student_new_null();
    s_new = sk_student_new(student_cmp);

    s2 =student_malloc();
    sk_student_push(s_new, s2);
    i = sk_student_find(s_new, s2);

    s1 = student_malloc();
    sk_student_push(s, s1);
    num = sk_student_num(s);

    for (i=0; i<num; ++i) {
        student *one = sk_student_value(s, i);
        printf("student name: %s\n", one->name);
        printf("student age: %d\n", one->age);
        printf("student other info: %s\n\n\n", one->other_info);
    }

    sk_student_pop_free(s, student_free);
    sk_student_pop_free(s_new, student_free);
    return 0;
}

