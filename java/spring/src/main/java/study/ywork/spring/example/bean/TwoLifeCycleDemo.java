package study.ywork.spring.example.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import study.ywork.spring.bean.lifecycle.MyTwoBean;
import study.ywork.spring.bean.lifecycle.OtherBean;

@Configuration
public class TwoLifeCycleDemo {
    // 懒加载: 按需加载
    @Bean
    @Lazy
    public MyTwoBean getMyTwoBean() {
        return new MyTwoBean();
    }

    @Bean
    public OtherBean otherBean() {
        return new OtherBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TwoLifeCycleDemo.class);
        System.out.println("-- 获取BEAN对象 --");
        MyTwoBean bean = context.getBean(MyTwoBean.class);
        bean.doSomething();
        context.close();
        System.out.println("-- 完成 --");
    }
}