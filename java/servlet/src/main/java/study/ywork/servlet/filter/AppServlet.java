package study.ywork.servlet.filter;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static study.ywork.servlet.constant.MediaType.TEXT_HTML_UTF8;

import java.io.IOException;
import java.io.PrintWriter;

/*
 * 测试错误过滤器的例子
 */
@WebServlet(name = "appServlet", urlPatterns = { "/app/*" })
public class AppServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String BR = "<br/>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(TEXT_HTML_UTF8);

        DispatcherType dispatcherType = req.getDispatcherType();
        String requestURI = req.getRequestURI();
        StringBuilder error = new StringBuilder("");
        error.append("请求路径: ").append(requestURI).append(BR);
        error.append("分派器类型: ").append(dispatcherType).append(BR);

        if (requestURI.endsWith("test")) {
            error.append("响应路径: ").append(requestURI).append(BR);
        } else {
            throw new ArithmeticException("模拟除零错误");
        }

        PrintWriter writer = resp.getWriter();
        writer.write(error.toString());
    }
}