package study.ywork.servlet.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName = "searchFilter", urlPatterns = {
    "/search/*" }, initParams = @WebInitParam(name = "env", value = "dev"))
public class SearchFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if ("dev".equals(filterConfig.getInitParameter("env"))) {
            long time = System.currentTimeMillis();
            chain.doFilter(request, response);
            time = System.currentTimeMillis() - time;
            String url = request instanceof HttpServletRequest
                ? ((HttpServletRequest) request).getRequestURL().toString()
                : "N/A";
            System.out.println("完成请求所需的时间: " + time + "ms");
            System.out.println("请求的URL: " + url);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        // 不做事儿
    }
}