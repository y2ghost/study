package study.ywork.rabbitmq.client;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class Recv {
    private static final Logger log = LoggerFactory.getLogger(Recv.class);
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = getFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            log.info(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info("[x] Received '{}'", message);
            };

            while (true) {
                channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
                });
            }
        }
    }
}
