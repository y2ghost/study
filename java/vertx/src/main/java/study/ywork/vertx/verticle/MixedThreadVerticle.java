package study.ywork.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CountDownLatch;

// 演示和非vertx的线程代码集成的示例
public class MixedThreadVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(MixedThreadVerticle.class);

    @Override
    public void start() {
        Context context = vertx.getOrCreateContext();
        new Thread(() -> {
            try {
                run(context);
            } catch (InterruptedException e) {
                logger.error("线程运行失败", e);
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void run(Context context) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        logger.info("我是模拟的非vertx线程");
        context.runOnContext(v -> {
            logger.info("我现在位于event-loop线程上");
            vertx.setTimer(1000, id -> {
                logger.info("最后一次计数器");
                latch.countDown();
            });
        });
        logger.info("等待计数器结束...");
        latch.await();
        logger.info("再见!");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MixedThreadVerticle());
    }
}
