package study.ywork.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 组件部署示例
public class DeployerVerticle {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Deployer());
    }

    public static class EmptyVerticle extends AbstractVerticle {
        private final Logger logger = LoggerFactory.getLogger(EmptyVerticle.class);

        @Override
        public void start() {
            logger.info("EmptyVerticle已启动");
        }

        @Override
        public void stop() {
            logger.info("EmptyVerticle已停止");
        }
    }

    public static class Deployer extends AbstractVerticle {
        private final Logger logger = LoggerFactory.getLogger(Deployer.class);

        @Override
        public void start() {
            long delay = 1000;
            for (int i = 0; i < 50; i++) {
                // 每一秒部署EmptyVerticle实例
                vertx.setTimer(delay, id -> deploy());
                delay = delay + 1000;
            }
        }

        // 采用异步部署的方式并五秒后撤销部署
        private void deploy() {
            vertx.deployVerticle(new EmptyVerticle(), ar -> {
                if (ar.succeeded()) {
                    String id = ar.result();
                    logger.info("成功部署 {}", id);
                    vertx.setTimer(5000, tid -> undeployLater(id));
                } else {
                    logger.error("错误部署", ar.cause());
                }
            });
        }

        private void undeployLater(String id) {
            vertx.undeploy(id, ar -> { // <4>
                if (ar.succeeded()) {
                    logger.info("{} 已撤销部署", id);
                } else {
                    logger.error("{} 撤销部署失败", id);
                }
            });
        }
    }
}
