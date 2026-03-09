package study.ywork.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

// 服务器创建客户端需要引导客户端时，采用共享EventLoop的方法示例
public class BootstrapSharingEventLoopGroup {
    public void bootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                ChannelFuture connectFuture;

                @Override
                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.channel(NioSocketChannel.class).handler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                            System.out.println("Received data");
                        }
                    });
                    // 共享服务器创建的子EventLoop对象
                    bootstrap.group(ctx.channel().eventLoop());
                    connectFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8080));
                }

                @Override
                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)
                    throws Exception {
                    if (connectFuture.isDone()) {
                        // 处理数据
                    }
                }
            });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
