package study.ywork.esClient;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

public class MyRequestRetryHandler implements HttpRequestRetryHandler {
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= 3) {
            // 超过最大重试次数
            return false;
        }

        if (exception instanceof InterruptedIOException) {
            // 超时处理
            return false;
        }

        if (exception instanceof UnknownHostException) {
            // 未知主机
            return false;
        }

        if (exception instanceof ConnectTimeoutException) {
            // 连接拒绝
            return false;
        }

        if (exception instanceof SSLException) {
            // SSL握手异常
            return false;
        }

        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        return !(request instanceof HttpEntityEnclosingRequest);
    }
}
