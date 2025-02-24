package study.ywork.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.ssl.SslHandler;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;

// 处理聊天室交互的处理器
public class ChatHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;

    public ChatHttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, request,
                new DefaultFullHttpResponse(request.protocolVersion(), BAD_REQUEST, ctx.alloc().buffer(0)));
            return;
        }

        if (!GET.equals(request.method())) {
            sendHttpResponse(ctx, request,
                new DefaultFullHttpResponse(request.protocolVersion(), FORBIDDEN, ctx.alloc().buffer(0)));
            return;
        }

        // 发送主页信息
        if ("/".equals(request.uri()) || "/index.html".equals(request.uri())) {
            String webSocketLocation = getWebSocketLocation(ctx.pipeline(), request, wsUri);
            ByteBuf content = IndexPage.getContent(webSocketLocation);
            FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpUtil.setContentLength(response, content.readableBytes());
            sendHttpResponse(ctx, request, response);
        } else {
            sendHttpResponse(ctx, request,
                new DefaultFullHttpResponse(request.protocolVersion(), NOT_FOUND, ctx.alloc().buffer(0)));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        HttpResponseStatus responseStatus = res.status();
        if (responseStatus.code() != 200) {
            ByteBufUtil.writeUtf8(res.content(), responseStatus.toString());
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
        HttpUtil.setKeepAlive(res, keepAlive);
        ChannelFuture future = ctx.writeAndFlush(res);

        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (null != cp.get(SslHandler.class)) {
            protocol = "wss";
        }

        return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
    }
}
