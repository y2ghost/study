package study.ywork.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// verticle属于vertx的基础组件，完成特定的任务
public class HelloVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(HelloVerticle.class);
    private long counter = 1;

    @Override
    public void start() {
        vertx.setPeriodic(5000, id -> {
            logger.info("滴答定时器 {}", id);
        });

        vertx.createHttpServer().requestHandler(req -> {
            logger.info("请求 #{} 来自 {}", counter++, req.remoteAddress().host());
            req.response().putHeader("content-Type", "text/html; charset=utf-8").end("欢迎您!");
        }).listen(8080);

        logger.info("打开 http://localhost:8080/");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloVerticle());
    }
}
