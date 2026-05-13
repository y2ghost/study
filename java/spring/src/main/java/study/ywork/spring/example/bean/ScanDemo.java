package study.ywork.spring.example.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import study.ywork.spring.bean.scan.MyPrototypeBean;

@Configuration
@ComponentScan("study.ywork.spring.bean.scan")
public class ScanDemo {
    public static void main(String[] strings) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScanDemo.class);
        System.out.println("Spring容器启动并准备就绪");
        MyPrototypeBean bean = context.getBean(MyPrototypeBean.class);
        context.close();
        bean.doSomething();
    }
}
