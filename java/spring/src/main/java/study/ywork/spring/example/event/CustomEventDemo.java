package study.ywork.spring.example.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class CustomEventDemo {
    @Bean
    AListenerBean listenerBean() {
        return new AListenerBean();
    }

    @Bean
    MyEvenPublisherBean publisherMyBean() {
        return new MyEvenPublisherBean();
    }

    @Bean
    YyEvenPublisherBean publisherYyBean() {
        return new YyEvenPublisherBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomEventDemo.class);
        MyEvenPublisherBean myBean = context.getBean(MyEvenPublisherBean.class);
        myBean.sendMsg("我的一条测试消息");
        YyEvenPublisherBean yyBean = context.getBean(YyEvenPublisherBean.class);
        yyBean.sendMsg("你的一条测试消息");
        context.close();
    }

    private static class MyEvenPublisherBean {
        /*
         * 自定义事件类型，需要先发布你的事件，然后由对应事件的监听器处理
         */
        @Autowired
        ApplicationEventPublisher publisher;

        public void sendMsg(String msg) {
            publisher.publishEvent(new OneEvent(this, msg));
        }
    }

    /*
     * 不使用主动注入，采取另一种方式实现发布BEAN类
     */
    private static class YyEvenPublisherBean implements ApplicationEventPublisherAware {

        ApplicationEventPublisher publisher;

        public void sendMsg(String msg) {
            publisher.publishEvent(new TwoEvent(msg));

        }

        @Override
        public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            this.publisher = applicationEventPublisher;

        }
    }

    private static class AListenerBean {
        @EventListener
        public void onOneEvent(OneEvent event) {
            System.out.print("捕获事件: " + event.getMsg());
            System.out.println("  --  事件源: " + event.getSource());
        }

        @EventListener
        public void onTwoEvent(TwoEvent event) {
            System.out.print("捕获事件: " + event.getMsg());
        }
    }

    private static class OneEvent extends ApplicationEvent {
        private static final long serialVersionUID = 1L;
        private final String msg;

        public OneEvent(Object source, String msg) {
            super(source);
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

    private static class TwoEvent {
        private final String msg;

        public TwoEvent(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }
}
