package study.ywork.spring.example.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import java.util.Locale;

public class XmlDemo {
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
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:messages-xml/msg");
            return messageSource;
        }
    }

    public static class MyBean {
        @Autowired
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("app.name", new Object[] { "Joe" }, Locale.getDefault()));
        }
    }
}