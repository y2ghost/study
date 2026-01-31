package study.ywork.servlet.async;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class MyAsyncListener implements AsyncListener {
    Logger logger = Logger.getLogger(MyAsyncListener.class.getName());

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        logger.info("异步处理完毕");
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        logger.info("异步处理超时");
        writeToResponse(event, "异步处理超时 " + System.currentTimeMillis());
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        logger.info("异步处理出错");
        writeToResponse(event, event.getThrowable().getMessage());
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        logger.info("异步处理开始");
    }

    private void writeToResponse(AsyncEvent event, String message) throws IOException {
        ServletResponse response = event.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        out.write(message);
    }
}
