package study.ywork.netty.handler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/* 
 * 自己维护资源时，特别注意资源的管理
 * 可以使用netty内置的泄漏检测机制协助定位资源管理出现的问题
 * java -Dio.netty.leakDetectionLevel=ADVANCED
 */

@Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {
    // 自定义实现channelRead方法时，需要显式的释放资源
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ReferenceCountUtil.release(msg);
    }
}
