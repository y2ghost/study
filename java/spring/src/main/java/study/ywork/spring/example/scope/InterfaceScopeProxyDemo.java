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
 * JDK接口代理类实现单例模式类里面获取SCOPE_PROTOTYPE实例的BEAN对象
 */
@Configuration
public class InterfaceScopeProxyDemo {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            InterfaceScopeProxyDemo.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);
        bean.showMessage();
        context.close();
    }

    interface IPrototype {

        String getDateTime();
    }

    static class MyPrototypeBean implements IPrototype {
        private String dateTimeString = LocalDateTime.now().toString();

        public String getDateTime() {
            return dateTimeString;
        }
    }

    static class MySingletonBean {
        @Autowired
        private IPrototype prototypeBean;

        public void showMessage() {
            System.out.println("为您报时 " + prototypeBean.getDateTime());
        }
    }
}
