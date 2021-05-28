package study.ywork.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CacheContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Cache Context初始化 " + this);
        String cache = "我就是缓存对象";
        event.getServletContext().setAttribute("TEST_CACHE", cache);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 不做事儿
    }
}
