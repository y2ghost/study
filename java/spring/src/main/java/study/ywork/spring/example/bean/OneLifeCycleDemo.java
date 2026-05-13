package study.ywork.spring.example.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.bean.lifecycle.MyOneBean;
import study.ywork.spring.bean.lifecycle.OtherBean;

@Configuration
public class OneLifeCycleDemo {
    // 定义初始化和销毁函数
    @Bean(initMethod = "myPostConstruct", destroyMethod = "cleanUp")
    public MyOneBean getMyOneBean() {
        return new MyOneBean();
    }

    @Bean
    public OtherBean otherBean() {
        return new OtherBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OneLifeCycleDemo.class);
        System.out.println("-- 获取BEAN对象 --");
        MyOneBean bean = context.getBean(MyOneBean.class);
        bean.doSomething();
        context.close();
        System.out.println("-- 完成 --");
    }
}