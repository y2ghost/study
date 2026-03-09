package study.ywork.netty.logevent;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

// 广播日志
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
            .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws Exception {
        Channel ch = bootstrap.bind(0).sync().channel();
        long position = 0;
        for (int i = 0; i < 100; i++) {
            long len = file.length();
            if (len < position) {
                position = len;
            } else if (len > position) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(position);

                while (true) {
                    String line = raf.readLine();
                    if (null == line) {
                        break;
                    }

                    ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }

                position = raf.getFilePointer();
                raf.close();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("请提供参数: port logfile");
            System.exit(1);
        }

        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
            new InetSocketAddress("255.255.255.255", Integer.parseInt(args[0])), new File(args[1]));
        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
