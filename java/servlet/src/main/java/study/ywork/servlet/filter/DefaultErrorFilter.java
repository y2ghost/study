package study.ywork.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebFilter;

/*
 * 错误处理过滤器，只有分派器的类型和过滤URL地址同时匹配才会生效
 */
@WebFilter(filterName = "defaultErrorFilter", urlPatterns = "/*", dispatcherTypes = { DispatcherType.ERROR })
public class DefaultErrorFilter implements Filter {
    private static final String BR = "<br/>";

    @Override
    public void init(FilterConfig filterConfig) {
        // 不做事儿
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException {
        chain.doFilter(req, resp);
        StringBuilder error = new StringBuilder("defaultErrorFilter拦截错误如下:");
        Exception exception = (Exception) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        error.append(BR);
        error.append("异常: ").append(exception).append(BR);
        Integer code = (Integer) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        error.append("状态码: ").append(code).append(BR);
        String requestUri = (String) req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        error.append("请求路径: ").append(requestUri).append(BR);
        error.append("分派器类型: ").append(req.getDispatcherType()).append(BR);
        PrintWriter writer = resp.getWriter();
        writer.printf("%s%s", error.toString(), BR);
    }

    @Override
    public void destroy() {
        // 不做事儿
    }
}