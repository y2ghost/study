package study.ywork.servlet.filter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * 通用错误处理器
 */
@WebServlet(name = "defaultErrorServlet", urlPatterns = { "/errors/default" })
public class DefaultErrorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = resp.getWriter();
        writer.printf("通用错误处理器:<br/>分派器类型: %s<br/>", req.getDispatcherType());
    }
}