package study.ywork.servlet.registration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_HTML_UTF8);
        Pagable page = resolvePage(req);
        if (page != null) {
            resp.getWriter().write("页面模型信息: " + page.getPageModelInfo() + "<br/>");
            resp.getWriter().write("页面视图信息: " + page.getPageViewInfo());
        }
    }

    @SuppressWarnings("unchecked")
    private Pagable resolvePage(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        Object obj = req.getServletContext().getAttribute("pages");

        if (obj instanceof List) {
            Optional<Pagable> first = ((List<Pagable>) obj).stream().filter(p -> path.equals(p.getPath())).findFirst();
            if (first.isPresent()) {
                return first.get();
            }
        }

        return null;
    }
}
