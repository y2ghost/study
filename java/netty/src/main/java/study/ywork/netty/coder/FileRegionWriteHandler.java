package study.ywork.netty.coder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.File;
import java.io.FileInputStream;

// 使用FileRegion处理大数据传输示例
public class FileRegionWriteHandler extends ChannelInboundHandlerAdapter {
    private static final Channel DEMO_CHANNEL = new NioSocketChannel();
    private static final File DEMO_FILE = new File("/tmp/bigData");

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        File file = DEMO_FILE;
        Channel channel = DEMO_CHANNEL;
        try (FileInputStream in = new FileInputStream(file)) {
            FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
            channel.writeAndFlush(region).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        // 处理异常的情况
                    }
                }
            });
        }
    }
}
