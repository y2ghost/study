package study.ywork.servlet.listener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;
import java.io.IOException;

@WebServlet(name = "cacheServlet", urlPatterns = { "/cache/*" }, loadOnStartup = 1)
public class CacheServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_HTML_UTF8);
        Object cache = req.getServletContext().getAttribute("TEST_CACHE");
        if (cache instanceof String) {
            resp.getWriter().println(cache);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("Servlet加载 " + this);
    }
}