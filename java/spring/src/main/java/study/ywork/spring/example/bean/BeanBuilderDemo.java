package study.ywork.spring.example.bean;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class BeanBuilderDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinitionBuilder b = BeanDefinitionBuilder.rootBeanDefinition(MyBean.class)
            .addPropertyValue("str", "动态修改值");
        beanFactory.registerBeanDefinition("myBean", b.getBeanDefinition());
        MyBean bean = beanFactory.getBean(MyBean.class);
        bean.doSomething();
    }

    @SuppressWarnings("unused")
    private static class MyBean {
        private String str;

        public void setStr(String str) {
            this.str = str;
        }

        public void doSomething() {
            System.out.println("来自MyBean " + str);
        }
    }
}
