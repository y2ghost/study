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

/**
 * COOKIE技术示例 缺点是客户端可以屏蔽 主要的Cookie配置类，控制CookieInfoServlet和CookieClassServlet类的行为
 * 删除Cookie需要创建同名的对象，设置maxAge = 0
 */
@WebServlet(name = "CookieConfigServlet", urlPatterns = { "/cookieconfig" })
public class CookieConfigServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.print("<html><head>" + "<title>Cookie配置</title>" + "<style>table {" + "font-size:small;"
            + "background:NavajoWhite }</style>" + "</head><body>" + MENU + "请您进行下面的配置:" + "<form method='post'>"
            + "<table>" + "<tr><td>字体大小: </td>" + "<td><select name='titleFontSize'>" + "<option>large</option>"
            + "<option>x-large</option>" + "<option>xx-large</option>" + "</select></td>" + "</tr>"
            + "<tr><td>字体样式: </td>" + "<td><select name='titleStyleAndWeight' multiple>" + "<option>italic</option>"
            + "<option>bold</option>" + "</select></td>" + "</tr>" + "<tr><td>最大记录数: </td>"
            + "<td><select name='maxRecords'>" + "<option>5</option>" + "<option>10</option>" + "</select></td>"
            + "</tr>" + "<tr><td rowspan='2'>" + "<input type='submit' value='设置'/></td>" + "</tr>" + "</table>"
            + "</form>" + "</body></html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maxRecords = request.getParameter("maxRecords");
        String[] titleStyleAndWeight = request.getParameterValues("titleStyleAndWeight");
        String titleFontSize = request.getParameter("titleFontSize");
        response.addCookie(new Cookie("maxRecords", maxRecords));
        response.addCookie(new Cookie("titleFontSize", titleFontSize));

        // 删除titleFontWeight 和 titleFontStyle cookies
        Cookie cookie = new Cookie("titleFontWeight", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        cookie = new Cookie("titleFontStyle", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if (titleStyleAndWeight != null) {
            for (String style : titleStyleAndWeight) {
                if (style.equals("bold")) {
                    response.addCookie(new Cookie("titleFontWeight", "bold"));
                } else if (style.equals("italic")) {
                    response.addCookie(new Cookie("titleFontStyle", "italic"));
                }
            }
        }

        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.println("<html><head>" + "<title>用户偏好</title>" + "</head><body>" + MENU
            + "您的cookie配置已经设置<br/><br/>最大记录数: " + maxRecords + "<br/>字体大小: " + titleFontSize + "<br/>字体样式: ");

        if (titleStyleAndWeight != null) {
            writer.println("<ul>");
            for (String style : titleStyleAndWeight) {
                writer.print("<li>" + style + "</li>");
            }

            writer.println("</ul>");
        }

        writer.println("</body></html>");
    }
}