package study.ywork.vertx.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

public class Echo {
    private static int numberOfConnections = 0;
    private static final Logger logger = LoggerFactory.getLogger(Echo.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.createNetServer().connectHandler(Echo::handleNewClient).listen(3000);
        vertx.setPeriodic(5000, id -> logger.info(howMany()));
        vertx.createHttpServer().requestHandler(request -> request.response().end(howMany())).listen(8080);
    }

    private static void handleNewClient(NetSocket socket) {
        numberOfConnections++;
        socket.handler(buffer -> {
            socket.write(buffer);
            if (buffer.toString().endsWith("/quit\n")) {
                socket.close();
            }
        });
        socket.closeHandler(v -> numberOfConnections--);
    }

    private static String howMany() {
        return String.format("已建立%d个连接", numberOfConnections);
    }
}
