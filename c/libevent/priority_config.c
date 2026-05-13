// 避免优先级反转的配置示例
void avoid_priority_inversion(void)
{
    struct event_config *cfg = event_config_new();
    if (!cfg) {
        // 处理错误
        return;
    }

    /*
     * 两个优先级的事件
     * 配置检查优先级1的事件为100msec或5次回调后
     * 采取检查优先级0的事件
     */
    struct timeval msec_100 = { 0, 100*1000 };
    event_config_set_max_dispatch_interval(cfg, &msec_100, 5, 1);
    struct event_base *base = event_base_new_with_config(cfg);

    if (!base) {
        // 处理错误
        return;
    }

    event_base_priority_init(base, 2);
}

