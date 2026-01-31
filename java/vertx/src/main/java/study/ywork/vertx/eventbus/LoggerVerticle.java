package study.ywork.vertx.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DecimalFormat;

public class LoggerVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(LoggerVerticle.class);
    private final DecimalFormat format = new DecimalFormat("#.##");

    @Override
    public void start() {
        EventBus bus = vertx.eventBus();
        bus.<JsonObject>consumer("sensor.updates", msg -> {
            JsonObject body = msg.body();
            String id = body.getString("id");
            String temperature = format.format(body.getDouble("temp"));
            logger.info("传感器{}报告温度: {}度", id, temperature);
        });
    }
}
