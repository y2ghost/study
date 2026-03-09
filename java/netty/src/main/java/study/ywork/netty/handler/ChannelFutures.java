package study.ywork.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

// 添加ChannelFutureListener到ChannelFuture
public class ChannelFutures {
    private static final Channel DEMO_CHANNEL = new NioSocketChannel();
    private static final ByteBuf DEMO_BUFFER = Unpooled.buffer(1024);

    public static void addingChannelFutureListener() {
        Channel channel = DEMO_CHANNEL;
        ByteBuf someMessage = DEMO_BUFFER;
        ChannelFuture future = channel.write(someMessage);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    System.err.println(future.cause().getMessage());
                    future.channel().close();
                }
            }
        });
    }

    private ChannelFutures() {
    }
}
