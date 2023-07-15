module msg.service.provider.swing {
    requires msg.service.api;
    requires java.desktop;
    // 服务提供同一个接口只能存在一个
    // provides msg.service.MsgService with msg.provider.swing.MsgServiceImpl;
    provides msg.service.MsgService with msg.provider.swing.MsgServiceProvider;
}

