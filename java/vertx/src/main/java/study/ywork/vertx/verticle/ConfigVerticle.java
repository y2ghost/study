package study.ywork.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 传递配置数据示例
public class ConfigVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(ConfigVerticle.class);

    @Override
    public void start() {
        logger.info("n = {}", config().getInteger("n", -1));
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        for (int n = 0; n < 4; n++) {
            JsonObject conf = new JsonObject().put("n", n);
            DeploymentOptions opts = new DeploymentOptions().setConfig(conf).setInstances(n);
            vertx.deployVerticle("study.ywork.vertx.verticle.ConfigVerticle", opts);
        }
    }
}
