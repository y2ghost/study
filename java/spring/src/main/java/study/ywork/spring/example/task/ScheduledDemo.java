package study.ywork.spring.example.task;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalTime;

public class ScheduledDemo {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean myBean = context.getBean(MyBean.class);
        System.out.printf("调用任务方法线程: %s%n", Thread.currentThread().getName());
        myBean.runTask();
        System.out.println("调用任务方法结束");
        Thread.sleep(5000);
        context.close();
    }

    @EnableScheduling
    @Configuration
    static class MyConfig {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }
    }

    static class MyBean {
        @Scheduled(fixedRate = 1000)
        public void runTask() {
            System.out.printf("运行调度任务" + " 线程: %s, 时间: %s%n", Thread.currentThread().getName(), LocalTime.now());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}
