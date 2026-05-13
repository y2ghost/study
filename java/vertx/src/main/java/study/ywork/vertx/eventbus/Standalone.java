package study.ywork.vertx.eventbus;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

//单节点部署组件的方式示例
public class Standalone {
    private static final String PACKAGE_NAME = "study.ywork.vertx.eventbus";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(PACKAGE_NAME+".HeatSensorVerticle", new DeploymentOptions().setInstances(4));
        vertx.deployVerticle(PACKAGE_NAME+".LoggerVerticle");
        vertx.deployVerticle(PACKAGE_NAME+".SensorDataVerticle");
        vertx.deployVerticle(PACKAGE_NAME+".HttpVerticle");
    }
}