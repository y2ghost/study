package study.ywork.spring.example.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

/*
 * CGLIB代理类实现单例模式类里面获取SCOPE_PROTOTYPE实例的BEAN对象
 */
@Configuration
public class ClassScopeProxyDemo {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ClassScopeProxyDemo.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);
        bean.showMessage();
        context.close();
    }

    static class MyPrototypeBean {
        private String dateTimeString = LocalDateTime.now().toString();

        public String getDateTime() {
            return dateTimeString;
        }
    }

    static class MySingletonBean {
        @Autowired
        private MyPrototypeBean prototypeBean;

        public void showMessage() {
            System.out.println("为您报时 " + prototypeBean.getDateTime());
        }
    }
}
