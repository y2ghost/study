package study.ywork.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

public class WriteHandlers {
    // 从ChannelHandlerContext访问Channel对象
    public static void writeViaChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channel.write(Unpooled.copiedBuffer("ctx channel", CharsetUtil.UTF_8));
    }

    // 从ChannelHandlerContext访问ChannelPipeline对象
    public static void writeViaChannelPipeline(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.write(Unpooled.copiedBuffer("ctx in pipeline", CharsetUtil.UTF_8));
    }

    // 缓冲区数据发送到下一个ChannelHandler
    public static void writeViaChannelHandlerContext(ChannelHandlerContext ctx) {
        ctx.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
    }

    private WriteHandlers() {
    }
}
