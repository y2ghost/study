package study.ywork.spring.example.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class BaseEventDemo {
    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BaseEventDemo.class);
        System.out.println("日志开始");
        System.out.println("-- Context停止 --");
        context.stop();
        System.out.println("-- Context开始 --");
        context.start();
        System.out.println("-- Context关闭 --");
        context.close();
    }

    private static class AListenerBean {
        @EventListener
        public void handleContextRefreshed(ContextRefreshedEvent event) {
            System.out.print("触发刷新事件: ");
            System.out.println(event);
        }

        @EventListener
        public void handleContextStarted(ContextStartedEvent event) {
            System.out.print("触发开始事件: ");
            System.out.println(event);
        }

        @EventListener
        public void handleContextStopped(ContextStoppedEvent event) {
            System.out.print("触发停止事件: ");
            System.out.println(event);
        }

        @EventListener
        public void handleContextClosed(ContextClosedEvent event) {
            System.out.print("触发关闭事件: ");
            System.out.println(event);
        }
    }
}