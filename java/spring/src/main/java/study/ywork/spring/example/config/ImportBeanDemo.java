package study.ywork.spring.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class ImportBeanDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
        context.close();
    }

    @Configuration
    @Import(MyBeanRegistrar.class)
    static class MyConfig {
        @Bean
        ClientBean clientBean() {
            return new ClientBean();
        }
    }

    private static class MyBeanRegistrar implements ImportBeanDefinitionRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
            GenericBeanDefinition gbd = new GenericBeanDefinition();
            gbd.setBeanClass(AppBean.class);
            gbd.getPropertyValues().addPropertyValue("str", "注册时设置的值");
            registry.registerBeanDefinition("appBean", gbd);
        }
    }

    private static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething() {
            appBean.process();
        }
    }

    @SuppressWarnings("unused")
    private static class AppBean {
        private String str;

        public void setStr(String str) {
            this.str = str;
        }

        public void process() {
            System.out.println(str);
        }
    }
}
