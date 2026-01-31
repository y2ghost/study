package study.ywork.spring.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;

public class DeferredSelectorDemo {
    public static void main(String[] args) {
        System.setProperty("myProp", "someValue");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    @Import(MyImportSelector.class)
    static class MainConfig {

        @Bean
        ClientBean clientBean() {
            return new ClientBean();
        }

        @Bean
        AppBean appBean() {
            return new AppBean("来自主配置类");
        }

    }

    static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething() {
            System.out.println(appBean.getMessage());
        }

    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            String prop = System.getProperty("myProp");
            if ("someValue".equals(prop)) {
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
            return new AppBean("来自MyConfig1");
        }
    }

    @Configuration
    static class MyConfig2 {
        @Bean
        AppBean appBean() {
            return new AppBean("来自MyConfig2");
        }
    }
}
