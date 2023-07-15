package study.ywork.spring.example.bean;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanFactoryDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
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

    private static class MyConfigBean implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

            GenericBeanDefinition bd = new GenericBeanDefinition();
            bd.setBeanClass(MyBean.class);
            bd.getPropertyValues().add("strProp", "动态添加的值");
            ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("myBeanName", bd);
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