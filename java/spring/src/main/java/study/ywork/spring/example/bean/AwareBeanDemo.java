package study.ywork.spring.example.bean;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/*
 * BEAN知晓ApplicationContext对象的例子
 * 也演示了单例BEAN里面如何做到SCOPE_PROTOTYPE生命周期的BEAN获取例子
 */
@Configuration
public class AwareBeanDemo {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    @Bean
    public MySingletonBean singletonBean() {
        return new MySingletonBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AwareBeanDemo.class);
        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
        context.close();
    }

    private static class MyPrototypeBean {
        private String dateTimeString = LocalDateTime.now().toString();

        public String getDateTime() {
            return dateTimeString;
        }
    }

    private static class MySingletonBean {
        @Autowired
        private ApplicationContext context;

        public void showMessage() {
            MyPrototypeBean bean = context.getBean(MyPrototypeBean.class);
            System.out.println("获取的时间是 " + bean.getDateTime());
        }
    }
}