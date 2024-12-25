package study.ywork.rabbitmq.client;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class ReceiveLogHeader {
    private static final Logger log = LoggerFactory.getLogger(ReceiveLogHeader.class);
    private static final String EXCHANGE_NAME = "header_test";

    public static void main(String[] argv) throws Exception {
        if (argv.length < 1) {
            log.error("Usage: ReceiveLogsHeader queueName [headers]...");
            System.exit(1);
        }

        ConnectionFactory factory = getFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);
            String routingKeyFromUser = "ourTestRoutingKey";
            String queueInputName = argv[0];
            Map<String, Object> headers = new HashMap<>();

            for (int i = 1; i < argv.length; i = i + 2) {
                String key = argv[i];
                String val = argv[i + 1];
                headers.put(key, val);
                log.info("Binding header {} and value {} to queue {}", key, val, queueInputName);
            }

            String queueName = channel.queueDeclare(queueInputName, true, false, false, null).getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, routingKeyFromUser, headers);
            log.info(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info(" [x] Received '{}':'{}}'", delivery.getEnvelope().getRoutingKey(), message);
            };
            while (true) {
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            }
        }
    }
}
