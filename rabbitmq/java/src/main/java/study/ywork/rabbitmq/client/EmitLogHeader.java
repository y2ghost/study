package study.ywork.rabbitmq.client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static study.ywork.rabbitmq.client.ConnectionUtils.getFactory;

public class EmitLogHeader {
    private static final Logger log = LoggerFactory.getLogger(EmitLogHeader.class);
    private static final String EXCHANGE_NAME = "header_test";

    public static void main(String[] argv) throws Exception {
        if (argv.length < 1) {
            log.error("Usage: EmitLogHeader message queueName [headers]...");
            System.exit(1);
        }

        String routingKey = "ourTestRoutingKey";
        String message = argv[0];
        Map<String, Object> headers = new HashMap<>();

        for (int i = 1; i < argv.length; i = i + 2) {
            String key = argv[i];
            String val = argv[i + 1];
            log.info("Adding header {} with value {} to Map", key, val);
            headers.put(key, val);
        }

        ConnectionFactory factory = getFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);

            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.deliveryMode(MessageProperties.PERSISTENT_TEXT_PLAIN.getDeliveryMode());
            builder.priority(MessageProperties.PERSISTENT_TEXT_PLAIN.getPriority());
            builder.headers(headers);
            AMQP.BasicProperties theProps = builder.build();
            channel.basicPublish(EXCHANGE_NAME, routingKey, theProps, message.getBytes(StandardCharsets.UTF_8));
            log.info(" [x] Sent message: '{}'", message);
        }
    }
}
