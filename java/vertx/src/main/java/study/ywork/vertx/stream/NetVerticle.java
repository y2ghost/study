package study.ywork.vertx.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetVerticle extends AbstractVerticle {
    private final Logger logger = LoggerFactory.getLogger(NetVerticle.class);

    @Override
    public void start() {
        logger.info("开始");
        vertx.createNetServer().connectHandler(this::handleClient).listen(3000);
    }

    private void handleClient(NetSocket socket) {
        logger.info("新的客户端");
        RecordParser.newDelimited("\n", socket).handler(buffer -> handleBuffer(socket, buffer))
                .endHandler(v -> logger.info("关闭客户端"));
    }

    private void handleBuffer(NetSocket socket, Buffer buffer) {
        String command = buffer.toString();
        switch (command) {
        case "/list":
            listCommand(socket);
            break;
        case "/play":
            vertx.eventBus().send("audio.play", "");
            break;
        case "/pause":
            vertx.eventBus().send("audio.pause", "");
            break;
        default:
            if (command.startsWith("/schedule ")) {
                schedule(command);
            } else {
                socket.write("Unknown command\n");
            }
        }
    }

    private void schedule(String command) {
        String track = command.substring(10);
        JsonObject json = new JsonObject().put("file", track);
        vertx.eventBus().send("audio.schedule", json);
    }

    private void listCommand(NetSocket socket) {
        vertx.eventBus().request("audio.list", "", reply -> {
            if (reply.succeeded()) {
                JsonObject data = (JsonObject) reply.result().body();
                data.getJsonArray("files").stream().forEach(name -> socket.write(name + "\n"));
            } else {
                logger.error("/list 出错", reply.cause());
            }
        });
    }
}
