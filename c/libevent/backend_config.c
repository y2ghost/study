// 过滤后端示例
void config_backend(void)
{
    for (int i=0; i<2; ++i) {
        struct event_config *cfg = event_config_new();
        /* 过滤select后端 */
        event_config_avoid_method(cfg, "select");
    
        if (i == 0) {
            event_config_require_features(cfg, EV_FEATURE_ET);
        }

        struct event_base *base = event_base_new_with_config(cfg);
        event_config_free(cfg);

        if (base) {
            break;
        }
    
        // 处理没有EV_FEATURE_ET类型的后端情况
    }
}
