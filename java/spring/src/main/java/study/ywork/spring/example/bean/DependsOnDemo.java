package study.ywork.spring.example.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/*
 * 演示@DependsOn注解用法
 */
@Configuration
public class DependsOnDemo {
    @Bean(initMethod = "initialize")
    @DependsOn("eventListener")
    public EventPublisherBean eventPublisherBean() {
        return new EventPublisherBean();
    }

    @Bean(name = "eventListener", initMethod = "initialize")
    public EventListenerBean eventListenerBean() {
        return new EventListenerBean();
    }

    public static void main(String[] strings) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DependsOnDemo.class);
        context.close();
    }

    private static class EventManager {
        private final List<Consumer<String>> listeners = new ArrayList<>();

        private EventManager() {
        }

        private static class SingletonHolder {
            private static final EventManager INSTANCE = new EventManager();
        }

        public static EventManager getInstance() {
            return SingletonHolder.INSTANCE;
        }

        public void publish(final String message) {
            listeners.forEach(l -> l.accept(message));
        }

        public void addListener(Consumer<String> eventConsumer) {
            listeners.add(eventConsumer);
        }
    }

    @SuppressWarnings("unused")
    private static class EventListenerBean {
        private void initialize() {
            EventManager.getInstance().addListener(s -> System.out.println("EventListenerBean中收到的事件: " + s));

        }
    }

    @SuppressWarnings("unused")
    private static class EventPublisherBean {
        public void initialize() {
            System.out.println("EventPublisherBean初始化");
            EventManager.getInstance().publish("从EventPublisherBean发布的事件");
        }
    }
}
