package study.ywork.servlet.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;

@WebServlet(name = "FormServlet", urlPatterns = { "/form" })
public class FormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TITLE = "订单示例";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>" + TITLE + "</title></head>");
        writer.println("<body><h1>" + TITLE + "</h1>");
        writer.println("<form method='post'>");
        writer.println("<table>");
        writer.println("<tr>");
        writer.println("<td>名称:</td>");
        writer.println("<td><input name='name'/></td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>地址:</td>");
        writer.println("<td><textarea name='address' " + "cols='40' rows='5'></textarea></td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>国家:</td>");
        writer.println("<td><select name='country'>");
        writer.println("<option>齐国</option>");
        writer.println("<option>秦国</option>");
        writer.println("</select></td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>快递方法:</td>");
        writer.println("<td><input type='radio' " + "name='deliveryMethod'" + " value='sf'/>顺丰");
        writer.println("<input type='radio' " + "name='deliveryMethod' " + "value='yt'/>圆通</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>快递要求:</td>");
        writer.println("<td><textarea name='instruction' " + "cols='40' rows='5'></textarea></td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>&nbsp;</td>");
        writer.println("<td><textarea name='instruction' " + "cols='40' rows='5'></textarea></td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>需要获取最新产品信息？:</td>");
        writer.println("<td><input type='checkbox' " + "name='catalogRequest'/></td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>&nbsp;</td>");
        writer.println("<td><input type='reset' value='重置'/>" + "<input type='submit' value='提交'/></td>");
        writer.println("</tr>");
        writer.println("</table>");
        writer.println("</form>");
        writer.println("</body>");
        writer.println("</html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType(TEXT_HTML_UTF8);
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>" + TITLE + "</title></head>");
        writer.println("</head>");
        writer.println("<body><h1>" + TITLE + "</h1>");
        writer.println("<table>");
        writer.println("<tr>");
        writer.println("<td>名称:</td>");
        writer.println("<td>" + request.getParameter("name") + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>地址:</td>");
        writer.println("<td>" + request.getParameter("address") + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>国家:</td>");
        writer.println("<td>" + request.getParameter("country") + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>快递要求:</td>");
        writer.println("<td>");
        String[] instructions = request.getParameterValues("instruction");

        if (instructions != null) {
            for (String instruction : instructions) {
                writer.println(instruction + "<br/>");
            }
        }

        writer.println("</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>快递方法:</td>");
        writer.println("<td>" + request.getParameter("deliveryMethod") + "</td>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>邮寄最新产品信息？:</td>");
        writer.println("<td>");

        if (request.getParameter("catalogRequest") == null) {
            writer.println("No");
        } else {
            writer.println("Yes");
        }

        writer.println("</td>");
        writer.println("</tr>");
        writer.println("</table>");
        writer.println("<div style='border:1px solid #ddd;" + "margin-top:40px;font-size:90%'>");
        writer.println("Debug Info<br/>");
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            writer.println(paramName + ": ");
            String[] paramValues = request.getParameterValues(paramName);

            for (String paramValue : paramValues) {
                writer.println(paramValue + "<br/>");
            }
        }

        writer.println("</div>");
        writer.println("</body>");
        writer.println("</html>");
    }
}