package study.ywork.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;

@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    private static final Charset UTF_8 = CharsetUtil.UTF_8;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf)msg;
        System.out.println("Server received: " + in.toString(UTF_8));
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
        throws Exception {
        ChannelFuture future = ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        System.err.println(cause.getMessage());
        ctx.close();
    }
}