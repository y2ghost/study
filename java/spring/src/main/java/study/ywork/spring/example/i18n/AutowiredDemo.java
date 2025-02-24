package study.ywork.spring.example.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import java.util.Locale;

public class AutowiredDemo {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    static class Config {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }

        @Bean
        @Primary
        public MessageSource messageSource() {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("messages/msg");
            return messageSource;
        }

        @Bean(name = "source2")
        public MessageSource messageSource2() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:messages/msg");
            messageSource.setDefaultEncoding("UTF-8");
            messageSource.setCacheMillis(500);
            return messageSource;
        }
    }

    static class MyBean {
        @Autowired
        private MessageSource messageSource;
        @Autowired
        @Qualifier("source2")
        private MessageSource messageSource2;

        public void doSomething() {
            System.out.println(messageSource.getMessage("app.name", new Object[] { "Joe" }, Locale.getDefault()));
            System.out.println(messageSource2.getMessage("app.name", new Object[] { "Joe" }, Locale.FRANCE));
        }
    }
}