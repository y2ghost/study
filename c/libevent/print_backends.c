#include <event2/event.h>
#include <stdio.h>

int main(int ac, char *av[])
{
    const char **methods = event_get_supported_methods();
    printf("版本%s. 可用后端函数:\n", event_get_version());

    for (int i=0; NULL!=methods[i]; i++) {
        printf("    %s\n", methods[i]);
    }

    return 0;
}

