package study.ywork.netty.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Charset UTF_8 = CharsetUtil.UTF_8;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println("Client received: " + in.toString(UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}