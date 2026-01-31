package study.ywork.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import java.util.Random;
import java.util.UUID;

public class HeatSensorVerticle extends AbstractVerticle {
    private final Random random = new Random();
    // 模拟温度传感器ID值
    private final String sensorId = UUID.randomUUID().toString();
    private double temperature = 21.0;

    @Override
    public void start() {
        scheduleNextUpdate();
    }

    private void scheduleNextUpdate() {
        vertx.setTimer(random.nextLong(5000) + 1000L, this::update);
    }

    private void update(long timerId) {
        temperature = temperature + (delta() / 10);
        JsonObject payload = new JsonObject().put("id", sensorId).put("temp", temperature);
        vertx.eventBus().publish("sensor.updates", payload);
        scheduleNextUpdate();
    }

    private double delta() {
        if (random.nextInt() > 0) {
            return random.nextGaussian();
        } else {
            return -random.nextGaussian();
        }
    }
}
