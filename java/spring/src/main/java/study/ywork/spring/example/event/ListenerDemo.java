package study.ywork.spring.example.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class ListenerDemo {
    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ListenerDemo.class);
        context.close();
    }

    private static class AListenerBean implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.print("触发刷新事件: ");
            System.out.println(event);
        }

    }
}