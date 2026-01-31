package study.ywork.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

// 用于演示阻塞的情况
public class BlockEventVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.setTimer(1000, id -> {
            while (true) {
                // 不做事儿
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new BlockEventVerticle());
    }
}
