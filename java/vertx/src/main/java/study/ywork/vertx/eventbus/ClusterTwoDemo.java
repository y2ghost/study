package study.ywork.vertx.eventbus;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 集群部署示例二
public class ClusterTwoDemo {
    private static final String PACKAGE_NAME = "study.ywork.vertx.eventbus";
    private static final Logger logger = LoggerFactory.getLogger(ClusterTwoDemo.class);

    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions(), ar -> {
            if (ar.succeeded()) {
                logger.info("ClusterTwoDemo已经启动了");
                Vertx vertx = ar.result();
                vertx.deployVerticle(PACKAGE_NAME + ".HeatSensorVerticle", new DeploymentOptions().setInstances(4));
                vertx.deployVerticle(PACKAGE_NAME + ".LoggerVerticle");
                vertx.deployVerticle(PACKAGE_NAME + ".SensorDataVerticle");
                JsonObject conf = new JsonObject().put("port", 8081);
                vertx.deployVerticle(PACKAGE_NAME + ".HttpVerticle", new DeploymentOptions().setConfig(conf));
            } else {
                logger.error("ClusterTwoDemo启动失败", ar.cause());
            }
        });
    }
}
