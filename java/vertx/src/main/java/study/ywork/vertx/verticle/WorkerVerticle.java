package study.ywork.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(WorkerVerticle.class);

    @Override
    public void start() {
        vertx.setPeriodic(10000, id -> {
            try {
                logger.info("睡觉中...");
                Thread.sleep(8000);
                logger.info("赶紧起床!");
            } catch (InterruptedException e) {
                logger.error("糟了，睡过头了!", e);
                Thread.currentThread().interrupt();
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions opts = new DeploymentOptions().setInstances(2).setWorker(true);
        vertx.deployVerticle("study.ywork.vertx.verticle.WorkerVerticle", opts);
    }
}
