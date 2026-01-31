package study.ywork.vertx.echo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventLoop {
    private final ConcurrentLinkedDeque<Event> events = new ConcurrentLinkedDeque<>();
    private final ConcurrentHashMap<String, Consumer<Object>> handlers = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(EventLoop.class);

    public static final class Event {
        private final String key;
        private final Object data;

        public Event(String key, Object data) {
            this.key = key;
            this.data = data;
        }
    }

    public EventLoop on(String key, Consumer<Object> handler) {
        handlers.put(key, handler);
        return this;
    }

    public void dispatch(Event event) {
        events.add(event);
    }

    public void run() {
        while (!(events.isEmpty() && Thread.interrupted())) {
            if (!events.isEmpty()) {
                Event event = events.pop();
                if (handlers.containsKey(event.key)) {
                    handlers.get(event.key).accept(event.data);
                } else {
                    logger.info("没有{}对应的处理器", event.key);
                }
            }
        }
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        new Thread(() -> {
            for (int n = 0; n < 6; n++) {
                delay(1000);
                eventLoop.dispatch(new EventLoop.Event("滴答", n));
            }

            eventLoop.dispatch(new EventLoop.Event("停止", null));
        }).start();

        new Thread(() -> {
            delay(2500);
            eventLoop.dispatch(new EventLoop.Event("通知", "新人66号"));
            delay(800);
            eventLoop.dispatch(new EventLoop.Event("通知", "熟人88号"));
        }).start();

        eventLoop.dispatch(new EventLoop.Event("通知", "大哥"));
        eventLoop.dispatch(new EventLoop.Event("广播", "小弟"));

        eventLoop.on("通知", s -> logger.info("通知 {}", s)).on("滴答", n -> logger.info("滴答 #{}", n))
                .on("停止", v -> eventLoop.stop()).run();

        logger.info("再见！");
    }

    private static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error("发生中断异常 {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
