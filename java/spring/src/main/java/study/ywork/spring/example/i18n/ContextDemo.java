package study.ywork.spring.example.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import java.util.Locale;

/*
 * 通过ApplicationContext获取国际化消息对象
 */
public class ContextDemo {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        System.out.println(context.getMessage("app.name", new Object[] { "Joe" }, Locale.getDefault()));
        context.close();

    }

    @Configuration
    public static class Config {
        @Bean
        public MessageSource messageSource() {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("messages/msg");
            return messageSource;
        }
    }
}
