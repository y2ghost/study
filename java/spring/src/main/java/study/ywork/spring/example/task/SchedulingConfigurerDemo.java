package study.ywork.spring.example.task;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import java.time.LocalTime;

public class SchedulingConfigurerDemo {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        System.out.println(" -- 5秒后退出程序-- ");
        Thread.sleep(5000);
        context.close();
    }

    @EnableScheduling
    @Configuration
    public static class MyConfig implements SchedulingConfigurer {
        @Override
        public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
            taskRegistrar.addFixedDelayTask(() -> {
                System.out.println("运行任务 : " + LocalTime.now());

            }, 500);
        }
    }
}
