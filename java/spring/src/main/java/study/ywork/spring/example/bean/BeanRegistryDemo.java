package study.ywork.spring.example.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanRegistryDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = (MyBean) context.getBean("myBeanName");
        bean.doSomething();
        context.close();
    }

    @Configuration
    static class MyConfig {
        @Bean
        static MyConfigBean myConfigBean() {
            return new MyConfigBean();
        }
    }

    private static class MyConfigBean implements BeanDefinitionRegistryPostProcessor {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
            GenericBeanDefinition bd = new GenericBeanDefinition();
            bd.setBeanClass(MyBean.class);
            bd.getPropertyValues().add("strProp", "动态修改的值");
            registry.registerBeanDefinition("myBeanName", bd);
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            // 不做事儿
        }
    }

    @SuppressWarnings("unused")
    private static class MyBean {
        private String strProp;

        public void setStrProp(String strProp) {
            this.strProp = strProp;
        }

        public void doSomething() {
            System.out.println("来自MyBean:  " + strProp);
        }
    }
}
