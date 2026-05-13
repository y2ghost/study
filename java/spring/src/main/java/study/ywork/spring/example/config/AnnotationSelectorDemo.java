package study.ywork.spring.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.annotation.AnnotationAttributes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AnnotationSelectorDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    @EnableSomeModule("someValue")
    static class MainConfig {
        @Bean
        ClientBean clientBean() {
            return new ClientBean();
        }
    }

    static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething() {
            System.out.println(appBean.getMessage());
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Import(MyImportSelector.class)
    public @interface EnableSomeModule {
        String value() default "";
    }

    public static class MyImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EnableSomeModule.class.getName(), false));

            String value = attributes.getString("value");
            if ("someValue".equals(value)) {
                return new String[] { MyConfig1.class.getName() };
            } else {
                return new String[] { MyConfig2.class.getName() };
            }
        }
    }

    static class AppBean {
        private String message;

        public AppBean(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Configuration
    static class MyConfig1 {
        @Bean
        AppBean appBean() {
            return new AppBean("from config 1");
        }
    }

    @Configuration
    static class MyConfig2 {
        @Bean
        AppBean appBean() {
            return new AppBean("from config 2");
        }
    }
}
