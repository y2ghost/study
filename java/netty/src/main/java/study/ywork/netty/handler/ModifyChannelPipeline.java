package study.ywork.netty.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;

// 动态修改ChannelPipeline的布局
public class ModifyChannelPipeline {
    public static void modifyPipeline(ChannelPipeline pipeline) {
        FirstHandler firstHandler = new FirstHandler();
        pipeline.addLast("handler1", firstHandler);
        pipeline.addFirst("handler2", new SecondHandler());
        pipeline.addLast("handler3", new ThirdHandler());
        // 数据处理后可以动态移除处理器
        pipeline.remove("handler3");
        pipeline.remove(firstHandler);
        pipeline.replace("handler2", "handler4", new FourthHandler());
    }

    private static final class FirstHandler extends ChannelHandlerAdapter {

    }

    private static final class SecondHandler extends ChannelHandlerAdapter {

    }

    private static final class ThirdHandler extends ChannelHandlerAdapter {

    }

    private static final class FourthHandler extends ChannelHandlerAdapter {

    }

    private ModifyChannelPipeline() {
    }
}
