package study.ywork.spring.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Filter属于标准的一部分
 * Spring Security使用了DelegatingFilterProxy集中处理安全相关的业务
 * HandlerInterceptor属于Spring框架内容
 * 位于DispatcherServlet和Controller之间
 */
public class ApiKeyFilter extends GenericFilterBean {
    private final boolean enabled;

    public ApiKeyFilter(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException {
        try {
            if (enabled) {
                Authentication authentication = AuthenticationService.getAuthentication((HttpServletRequest) req);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(req, resp);
        } catch (Exception exp) {
            HttpServletResponse httpResponse = (HttpServletResponse) resp;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = httpResponse.getWriter();
            writer.print(exp.getMessage());
            writer.flush();
            writer.close();
        }
    }
}
