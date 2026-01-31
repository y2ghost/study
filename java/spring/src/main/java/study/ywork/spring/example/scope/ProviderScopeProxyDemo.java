package study.ywork.spring.example.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

/*
 * Spring ObjectProvider接口实现类实现单例模式类里面获取SCOPE_PROTOTYPE实例的BEAN对象
 */
@Configuration
public class ProviderScopeProxyDemo {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean("注册的MyPrototypeBean");
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    @Bean
    public BeanNameFormatter beanNameFormatterBean() {
        return new BeanNameFormatter();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            ProviderScopeProxyDemo.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        context.close();
    }

    static class BeanNameFormatter {
        public String formatName(String name) {
            return String.format("格式化Bean的名称: %s%n", name);
        }
    }

    static class MyPrototypeBean {
        @Autowired
        private BeanNameFormatter beanNameFormatter;
        private String dateTimeString = LocalDateTime.now().toString();
        private String name;

        public MyPrototypeBean(String name) {
            this.name = name;
        }

        public String getDateTime() {
            return dateTimeString;
        }

        public String getFormattedName() {
            return beanNameFormatter.formatName(name);
        }
    }

    static class MySingletonBean {
        @Autowired
        private ObjectProvider<MyPrototypeBean> provider;

        public void showMessage() {
            MyPrototypeBean bean = provider.getIfAvailable(() -> new MyPrototypeBean("默认的MyPrototypeBean"));
            System.out.printf("为您报时: %s 来自Bean: %s - 实例: %s%n", bean.getDateTime(), bean.getFormattedName(),
                System.identityHashCode(bean));
        }
    }
}
