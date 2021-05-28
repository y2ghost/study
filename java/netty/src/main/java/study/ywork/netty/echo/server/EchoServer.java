package study.ywork.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;

public class EchoServer {
    private static final int PORT = 8888;

    public EchoServer() {
    }

    public static void main(String[] args) throws Exception {
        new EchoServer().start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(PORT))
                .childHandler(buildInitializer());
            ChannelFuture f = b.bind().sync();
            System.out.println("listenning on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private static ChannelInitializer buildInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch)
                throws Exception {
                ch.pipeline().addLast(new EchoServerHandler());
            }
        };
    }
}