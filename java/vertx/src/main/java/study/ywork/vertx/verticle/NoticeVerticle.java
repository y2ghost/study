package study.ywork.vertx.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

// 处理异步通知的示例
public class NoticeVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(NoticeVerticle.class);

    @Override
    // 通知的值为Void类型，是因为vertx只关心是否组件部署成功与否
    public void start(Promise<Void> promise) {
        vertx.createHttpServer().requestHandler(req -> req.response().end("OK")).listen(8080, ar -> {
            if (ar.succeeded()) {
                promise.complete();
            } else {
                logger.error("发生错误: {}", ar.cause().getMessage());
                promise.fail(ar.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new NoticeVerticle());
    }
}
