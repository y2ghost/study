#include <string.h>
#include <openssl/lhash.h>

typedef struct student_st {
    char name[20];
    int age;
    char other_info[20];
} student;

static int student_cmp(const void *a, const void *b)
{
    const char *a_name = ((const student*)a)->name;
    const char *b_name = ((const student*)b)->name;
    return strcmp(a_name, b_name);
}

static void print_value(void *data)
{
    student *s = data;
    printf("student name: %s\n", s->name);
    printf("student age: %d\n", s->age);
    printf("student other info: %s\n\n\n", s->other_info);
}

static void print_value_arg(void *data, void *arg)
{
    student *s = data;
    int flag = *(int*)arg;
    printf("user input arg: %d\n", flag);
    printf("student name: %s\n", s->name);
    printf("student age: %d\n", s->age);
    printf("student other info: %s\n\n\n", s->other_info);
}

int main(int ac, char *av[])
{
    student s1 = {"zcp", 28, "hu bei"};
    student s2 = {"fxy", 24, "no info"};
    student s3 = {"skp", 22, "student"};
    student s4 = {"zzp", 30, "hu nan"};
    student *s[4] = {&s1, &s2, &s3, &s4};

    _LHASH *h = lh_new(NULL, student_cmp);
    if (NULL == h) {
        printf("hash create error!\n");
        return -1;
    }

    int i = 0;
    for (i=0; i<4; ++i) {
        lh_insert(h, s[i]);
    }

    int flag = 11;
    lh_doall_arg(h, print_value_arg, (void*)&flag);
    lh_doall(h, print_value);

    void *data = lh_retrieve(h, (const void*)"skp");
    if (NULL == data) {
        printf("can not look up skp!\n");
        lh_free(h);
        h = NULL;
        return -1;
    }

    student *s5 = data;
    print_value(s5);
    lh_free(h);
    h = NULL;
    return 0;
}


