package study.ywork.vertx.verticle;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 演示Context的用法
public class ContextExamples {
    private static final Logger logger = LoggerFactory.getLogger(ContextExamples.class);

    public static void main(String[] args) {
        createAndRun();
        dataAndExceptions();
    }

    private static void createAndRun() {
        Vertx vertx = Vertx.vertx();
        vertx.getOrCreateContext().runOnContext(v -> logger.info("ABC"));
        vertx.getOrCreateContext().runOnContext(v -> logger.info("123"));
    }

    private static void dataAndExceptions() {
        Vertx vertx = Vertx.vertx();
        Context ctx = vertx.getOrCreateContext();
        ctx.put("模拟键", "测试值");

        ctx.exceptionHandler(t -> {
            if ("模拟异常".equals(t.getMessage())) {
                logger.info("捕获了模拟异常");
            } else {
                logger.error("其他异常", t);
            }
        });

        ctx.runOnContext(v -> {
            throw new RuntimeException("模拟异常");
        });

        ctx.runOnContext(v -> {
            String value = ctx.get("模拟键");
            logger.info("模拟键 = {}", value);
        });
    }
}
