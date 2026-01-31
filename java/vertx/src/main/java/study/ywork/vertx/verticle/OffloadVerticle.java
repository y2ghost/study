package study.ywork.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OffloadVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(OffloadVerticle.class);

    @Override
    public void start() {
        vertx.setPeriodic(5000, id -> {
            logger.info("滴答");
            vertx.executeBlocking(this::blockingCode, this::resultHandler);
        });
    }

    private void blockingCode(Promise<String> promise) {
        logger.info("开始执行阻塞性的代码，另起工作线程执行");
        try {
            Thread.sleep(4000);
            logger.info("任务完成!");
            promise.complete("OK");
        } catch (InterruptedException e) {
            promise.fail(e);
            Thread.currentThread().interrupt();
        }
    }

    private void resultHandler(AsyncResult<String> ar) {
        if (ar.succeeded()) {
            logger.info("阻塞代码执行结果: {}", ar.result());
        } else {
            logger.error("阻塞代码执行失败", ar.cause());
        }
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new OffloadVerticle());
    }
}
