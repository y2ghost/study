package study.ywork.spring.example.bean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class InjectingBeanDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory context = new DefaultListableBeanFactory();

        // 定义和注册MyOtherBean
        GenericBeanDefinition beanOtherDef = new GenericBeanDefinition();
        beanOtherDef.setBeanClass(MyOtherBean.class);
        context.registerBeanDefinition("other", beanOtherDef);

        // 定义和注册myBean
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(MyBean.class);
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.addPropertyValue("otherBean", context.getBean("other"));
        beanDef.setPropertyValues(mpv);
        context.registerBeanDefinition("myBean", beanDef);

        // 使用MyBean示例
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    @SuppressWarnings("unused")
    private static class MyBean {
        private MyOtherBean otherBean;

        public void setOtherBean(MyOtherBean otherBean) {
            this.otherBean = otherBean;
        }

        public void doSomething() {
            otherBean.doSomething();
        }
    }

    private static class MyOtherBean {
        public void doSomething() {
            System.out.println("我是MyOtherBean的实例");
        }
    }
}
