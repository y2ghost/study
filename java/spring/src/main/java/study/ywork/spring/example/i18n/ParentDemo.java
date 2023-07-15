package study.ywork.spring.example.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import java.util.Locale;

/*
 * 子MessageSource会覆盖父MessageSource定义的消息
 */
public class ParentDemo {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    public static class Config {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }

        @Bean
        public MessageSource messageSource() {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("messages/msg2");
            ResourceBundleMessageSource parentMessageSource = new ResourceBundleMessageSource();
            parentMessageSource.setBasename("messages/msg");
            messageSource.setParentMessageSource(parentMessageSource);
            return messageSource;
        }
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("app.name", new Object[] { "Joe" }, Locale.getDefault()));
            System.out.println(messageSource.getMessage("app2.name", new Object[] { "Jackie" }, Locale.getDefault()));
        }
    }
}