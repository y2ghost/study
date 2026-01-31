package study.ywork.servlet.cookie;

import static study.ywork.servlet.constant.CookieConstants.MENU;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;

@WebServlet(name = "CookieClassServlet", urlPatterns = { "/cookieClass" })
public class CookieClassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final String[] methods = { "clone", "getComment", "getDomain", "getMaxAge", "getName", "getPath",
        "getSecure", "getValue", "getVersion", "isHttpOnly", "setComment", "setDomain", "setHttpOnly", "setMaxAge",
        "setPath", "setSecure", "setValue", "setVersion" };

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        Cookie maxRecordsCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("maxRecords")) {
                    maxRecordsCookie = cookie;
                    break;
                }
            }
        }

        int maxRecords = 5;
        if (maxRecordsCookie != null) {
            try {
                maxRecords = Integer.parseInt(maxRecordsCookie.getValue());
            } catch (NumberFormatException e) {
            }
        }

        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.print("<html><head>" + "<title>Cookie类别</title>" + "</head><body>" + MENU
            + "<div>下面是javax.servlet.http.Cookie类中的一些方法");
        writer.print("<ul>");

        for (int i = 0; i < maxRecords; i++) {
            writer.print("<li>" + methods[i] + "</li>");
        }

        writer.print("</ul>");
        writer.print("</div></body></html>");
    }
}