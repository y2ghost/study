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

@WebServlet(name = "CookieInfoServlet", urlPatterns = { "/cookieInfo" })
public class CookieInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        StringBuilder styles = new StringBuilder();
        styles.append(".title {");

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                switch (name) {
                case "titleFontSize":
                    styles.append("font-size:").append(value).append(";");
                    break;
                case "titleFontWeight":
                    styles.append("font-weight:").append(value).append(";");
                    break;
                case "titleFontStyle":
                    styles.append("font-style:").append(value).append(";");
                    break;
                default:
                    break;
                }
            }
        }

        styles.append("}");
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.print("<html><head>" + "<title>Cookie信息</title>" + "<style>" + styles.toString() + "</style>"
            + "</head><body>" + MENU + "<div class='title'>使用cookie的会话管理:</div>");
        writer.print("<div>");

        if (cookies == null) {
            writer.print("此HTTP响应中没有Cookie信息");
        } else {
            writer.println("<br/>Cookie在此HTTP响应:");
            for (Cookie cookie : cookies) {
                writer.println("<br/>" + cookie.getName() + ": " + cookie.getValue());
            }
        }

        writer.print("</div>");
        writer.print("</body></html>");
    }
}