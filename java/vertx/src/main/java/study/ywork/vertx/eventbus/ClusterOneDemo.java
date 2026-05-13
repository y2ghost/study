package study.ywork.vertx.eventbus;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 集群部署示例一
public class ClusterOneDemo {
    private static final String PACKAGE_NAME = "study.ywork.vertx.eventbus";
    private static final Logger logger = LoggerFactory.getLogger(ClusterOneDemo.class);

    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions(), ar -> {
            if (ar.succeeded()) {
                logger.info("ClusterOneDemo已经启动了");
                Vertx vertx = ar.result();
                vertx.deployVerticle(PACKAGE_NAME + ".HeatSensorVerticle", new DeploymentOptions().setInstances(4));
                vertx.deployVerticle(PACKAGE_NAME + ".HttpVerticle");
            } else {
                logger.error("ClusterOneDemo启动失败", ar.cause());
            }
        });
    }
}
