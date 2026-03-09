package study.ywork.spring.example.bean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import java.util.Date;

public class GenericBeanDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory context = new DefaultListableBeanFactory();
        GenericBeanDefinition gbd = new GenericBeanDefinition();
        gbd.setBeanClass(MyBean.class);
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("date", new Date());
        gbd.setPropertyValues(mpv);
        // 同样的属性设置可以如下代码:
        // gbd.getPropertyValues().addPropertyValue("date", new Date());

        context.registerBeanDefinition("myBeanName", gbd);
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    @SuppressWarnings("unused")
    private static class MyBean {
        private Date date;

        public void doSomething() {
            System.out.println("MyBean 日期: " + date);
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
