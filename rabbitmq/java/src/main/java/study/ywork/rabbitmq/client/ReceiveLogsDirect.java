package study.ywork.rabbitmq.client;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class ReceiveLogsDirect {
    private static final Logger log = LoggerFactory.getLogger(ReceiveLogsDirect.class);
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = getFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String queueName = channel.queueDeclare().getQueue();

            if (argv.length < 1) {
                log.error("Usage: ReceiveLogsDirect [info] [warning] [error]");
                System.exit(1);
            }

            for (String severity : argv) {
                channel.queueBind(queueName, EXCHANGE_NAME, severity);
            }
            log.info(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info(" [x] Received '{}':'{}'", delivery.getEnvelope().getRoutingKey(), message);
            };
            while (true) {
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            }
        }
    }
}
