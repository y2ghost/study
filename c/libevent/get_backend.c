#include <event2/event.h>
#include <stdio.h>

// 获取使用的后端示例
int main(int ac, char *av[])
{
    struct event_base *base = event_base_new();
    if (!base) {
        printf("获取event_base失败!\n");
    } else {
        printf("使用的后端: %s.\n", event_base_get_method(base));
        enum event_method_feature f = event_base_get_features(base);

        if ((f & EV_FEATURE_ET)) {
            printf("  Edge-triggered events支持.\n");
        }

        if ((f & EV_FEATURE_O1)) {
            printf("  O(1) events支持.\n");
        }

        if ((f & EV_FEATURE_FDS)) {
            printf("  All FD types支持.\n");
        }

        printf("\n");
    }

    event_base_free(base);
    return 0;
}

