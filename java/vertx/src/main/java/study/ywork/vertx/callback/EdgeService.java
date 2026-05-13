package study.ywork.vertx.callback;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class EdgeService {
    private static final String PACKAGE_NAME = "study.ywork.vertx.callback";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        for (int i = 0; i < 3; i++) {
            vertx.deployVerticle(PACKAGE_NAME + ".HeatSensor",
                    new DeploymentOptions().setConfig(new JsonObject().put("http.port", 3000 + i)));
        }

        vertx.deployVerticle(PACKAGE_NAME + ".SnapshotService");
        vertx.deployVerticle(PACKAGE_NAME + ".CollectorService");
        vertx.deployVerticle(PACKAGE_NAME + ".FutureCollectorService");
    }
}
