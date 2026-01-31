package study.ywork.servlet.registration;

import javax.servlet.Servlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.util.Locale;

/*
 * 代码动态注册HttpServlet例子
 */
@WebListener
public class ProgramListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Locale locale = Locale.getDefault();
        String countryCode = locale.getISO3Country();
        System.out.println("国家代码: " + countryCode);
        Class<? extends Servlet> cls = DefaultAppServlet.class;

        if (countryCode.equals("CHN")) {
            cls = ChinaAppServlet.class;
        }

        ServletRegistration.Dynamic registration = event.getServletContext().addServlet("programServlet", cls);
        registration.addMapping("/register/");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 不做事儿
    }
}