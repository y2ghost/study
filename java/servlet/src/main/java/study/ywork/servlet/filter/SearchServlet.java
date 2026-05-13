package study.ywork.servlet.filter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "searchServlet", urlPatterns = { "/search/*" })
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 此处模拟搜索耗时操作
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resp.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = resp.getWriter();
        writer.println("<html><head></head><body>测试搜索结果</body></html");
    }
}