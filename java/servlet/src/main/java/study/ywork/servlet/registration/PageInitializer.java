package study.ywork.servlet.registration;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@HandlesTypes({ Pagable.class })
public class PageInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> pageClasses, ServletContext ctx) throws ServletException {
        List<Pagable> pages = new ArrayList<>();
        if (pageClasses != null) {
            for (Class<?> pageClass : pageClasses) {
                if (!pageClass.isInterface() && !Modifier.isAbstract(pageClass.getModifiers())) {
                    try {
                        Pagable page = (Pagable) pageClass.getDeclaredConstructor().newInstance();
                        pages.add(page);
                    } catch (Throwable ex) {
                        throw new ServletException("实例化Pagable对象失败", ex);
                    }
                }
            }
        }

        if (!pages.isEmpty()) {
            ctx.setAttribute("pages", pages);
            ServletRegistration.Dynamic servletRegistration = ctx.addServlet("ageServlet", PageServlet.class);
            pages.forEach(p -> {
                servletRegistration.addMapping(p.getPath());
            });
        }
    }
}