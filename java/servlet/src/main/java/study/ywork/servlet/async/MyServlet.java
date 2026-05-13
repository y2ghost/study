package study.ywork.servlet.async;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "myServlet", urlPatterns = { "/async-test" }, asyncSupported = true)
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = resp.getWriter();
        AsyncContext asyncContext = req.startAsync();
        asyncContext.addListener(new MyAsyncListener());
        AsyncContext finalAsyncContext = asyncContext;

        asyncContext.start(() -> {
            String msg = task();
            writer.println(msg);
            finalAsyncContext.complete();
        });
    }

    private String task() {
        long start = System.currentTimeMillis();
        try {
            Long i = ThreadLocalRandom.current().nextLong(1L, 5L);
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "完成长期任务的时间 " + (System.currentTimeMillis() - start);
    }
}
