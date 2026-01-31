package study.ywork.servlet.example;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;

public class WelcomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.print("<html><head></head>" + "<body>欢迎测试 web.xml(WelcomeServlet)</body></html>");
    }
}