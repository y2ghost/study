package study.ywork.spring.example.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import java.io.IOException;
import java.util.Locale;

public class AwareDemo {
    public static void main(String[] args) throws IOException {
        // 设置本地化语言
        Locale.setDefault(Locale.FRANCE);
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
            // 默认配置文件名称
            messageSource.setBasename("messages/msg");
            return messageSource;
        }
    }

    public static class MyBean implements MessageSourceAware {
        private MessageSource messageSource;

        public void doSomething() {
            System.out.println(messageSource.getMessage("app.name", new Object[] { "Joe" }, Locale.getDefault()));
        }

        @Override
        public void setMessageSource(MessageSource messageSource) {
            this.messageSource = messageSource;

        }
    }
}